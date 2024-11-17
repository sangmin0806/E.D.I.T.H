# reviewer.py
import os
from pathlib import Path
import re
from app.chunking.get_code import GitLabCodeChunker
from app.services.embeddings import CodeEmbeddingProcessor
from langchain_core.output_parsers import StrOutputParser
from langchain.memory import ConversationBufferMemory
from langchain_core.prompts import ChatPromptTemplate
from langchain.text_splitter import TokenTextSplitter
from app.services.llm_model import LLMModel
import uuid
import json
import logging

logger = logging.getLogger(__name__)


def getCodeReview(url, token, projectId, branch, changes):
    chunker = None
    vectorDB = None
    uuid = generate_uuid()

    try:
        # 0. DB 초기화
        vectorDB = CodeEmbeddingProcessor(uuid)
        # 1. git Clone
        chunker = GitLabCodeChunker(
            gitlab_url=url,
            gitlab_token=token,
            project_id=projectId,
            local_path='./cloneRepo/' + projectId,
            branch=branch
        )

        # 2. 파일별 임베딩
        project_path = chunker.clone_project()
        if not project_path:
            return '', ''

        # 3. 리뷰 할 코드들 메서드 Chunking
        file_chunks = []

        # changes 에 포함된 파일 확장자 정보 미리 저장
        relevant_extensions = set()
        for change in changes:
            relevant_extensions.add(get_language_from_extension(change['path']))

        for root, _, files in os.walk(project_path):
            for file in files:
                file_path = Path(root) / file

                # 불필요한 파일/폴더 제외
                if ('.git' in str(file_path)
                        or any(part.startswith('.') for part in file_path.parts)
                        or any(part.startswith('node_modules') for part in file_path.parts)):
                    continue

                # 파일 언어 확인
                language = chunker.get_file_language(str(file_path))
                if not language:
                    continue

                # changes 의 path 필드에 존재하는 파일 확장자명만 임베딩
                if language in relevant_extensions:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        chunks = chunker.chunk_file(str(file_path), language)
                        file_chunks.extend(chunks)

        vectorDB.store_embeddings(file_chunks)

        review_queries = []  # path, diff (전문), 참고할 코드 (메서드)
        for change in changes:
            language = get_language_from_extension(change['path'])

            if (language == ''):
                continue
            removed_lines, added_lines = parse_git_diff(change['diff'])
            similar_codes = ["""def query_similar_code(self, code_snippet, n_results=5):
        try:
            results = self.db.similarity_search_with_score(
                query=code_snippet,
                k=n_results
            )
            related_codes = [doc.page_content for doc, score in results]
            return related_codes
        except Exception as e:
            logger.info(f"Error querying similar code: {e}")
            return []"""]
            code_chunks = []

            # 코드 임베딩
            for added_line in added_lines:
                method = chunker.chunk_code(added_line, language)
                code_chunks.extend(method)
            # 유사도 분석
            for code_chunk in code_chunks:
                similar_codes.append(vectorDB.query_similar_code(code_chunk, 5))
            review_queries.append([change['path'], change['diff'], similar_codes])

        # 5. 메서드 별 관련 코드 가져와 리트리버 생성, 질의
        llm_model = LLMModel()
        llm = llm_model.llm

        # 6. LLM 에 질의해 결과 반환
        result = get_code_review(projectId, review_queries, llm)
        return result


    except Exception as e:
        logger.info(f"오류 발생: {e}")
        return '', ''

    finally:
        # 리소스 정리
        if vectorDB:
            try:
                vectorDB.cleanup()
            except Exception as e:
                logger.info(f"Error cleaning up vectorDB: {e}")
        if chunker:
            try:
                chunker.cleanup_project_directory()
            except Exception as e:
                logger.info(f"Error cleaning up project directory: {e}")


def get_language_from_extension(file_name: str) -> str:
    extension = file_name.split('.')[-1].lower()  # 확장자 추출
    # 확장자별 언어 매핑
    language_map = {
        'py': 'python',
        'java': 'java',
        'js': 'javascript',
        'jsx': 'javascript',
        'ts': 'javascript',
        'tsx': 'javascript',
        'c': 'c',
        'cpp': 'cpp',
        'h': 'c',
        'hpp': 'cpp'
    }
    return language_map.get(extension, '')


def get_code_review(projectId, review_queries, llm):
    uuid = generate_uuid()
    portfolio_memory = ConversationBufferMemory(
        memory_key=f"{projectId}_portfolio_{uuid}",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 코드리뷰를 참고해 포트폴리오를 만들 해당 MR 의 기술스택, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )

    code_review_memory = ConversationBufferMemory(
        memory_key=f"{projectId}_code_review_{uuid}",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 요약본 으로 전체 코드리뷰를 작성하기 위해 중요한 기능, 수정 해야 할 사항, 에러 발생 원인, 트러블 슈팅을 요약해줘"""
    )

    review_prompt = ChatPromptTemplate.from_template("""
        아래 git diff 의 핵심 내용만 간단히 요약해주세요.

        파일: {file_path}

        ===git diff===
        {code_chunk}

        ==='이미 존재하는 참고 코드'===
        {similar_codes}

        ========================
        다음 항목별로 핵심만 간단히 작성해주세요:
        *0. 기능적으로 유사한 코드가 존재하는 경우 : [유사한 코드, 함수 가 있다고 알려줘]
        1. 핵심 기능: [해당 코드의 핵심 기능]
        2. 변경사항: [주요 기능/로직 변경 1-2줄 요약]
        3. 주의 필요: [잠재적 이슈나 개선필요 사항, Clean Code 지향]
        4. 수정 해야 할 사항: [기능적으로 중복되거나 반드시 수정해야 하는 부분을 중요하게, 코드를 포함해 간략히]
        5. 개선 사항: [해결된 문제점 있는 경우만]
        5. 참고 코드와 비교한 조언:
       - [참고 코드와의 비교: 개선 가능한 부분 (코딩 스타일, 성능, 재사용성, 유지보수성 중 어떤 영역에서 개선 가능한지)]
        ===
        
        * 중요: 꼭 필요한 내용만 간단히 작성해주세요.
    """)

    final_review_prompt = ChatPromptTemplate.from_template("""
        해당 MR의 전체 코드리뷰를 GitLab MR Comment 형식으로 작성해주세요.
        * 중요: 응답은 JSON 형식으로 다음 구조를 따라 작성해주세요:
        * techStack 은 ["JavaScript", "TypeScript", "HTML5", "CSS3", "Sass", "Bootstrap", "TailwindCSS", "React", "Angular", 
  "Vue", "Svelte", "jQuery", "Node", "Express", "NestJS", "NextJS", "NuxtJS", "Python", "Django", "Flask", 
  "Java", "Spring", "PHP", "Laravel", "Ruby", "Rails", "CSharp", "DotNet", "Cplusplus", "Go", "Rust", 
  "Swift", "Kotlin", "Docker", "Kubernetes", "AWS", "Firebase", "GoogleCloud", "Azure", "Heroku", 
  "MySQL", "PostgreSQL", "MongoDB", "Redis", "Elasticsearch", "GraphQL", "Apollo", "Git", "GitHub", 
  "GitLab", "Bitbucket", "Jenkins", "TravisCI", "CircleCI", "NGINX", "Vercel"] 해당 배열 안에서 골라줘
  
        {{
            "review": "<코드리뷰 내용을 HTML 형식으로 작성>",
            "techStack": ["사용된 기술스택 목록"]
        }}

        ===파일별 주요 변경사항===
        {history}

        # MR 전체 요약
        - [전체 변경사항 핵심 요약]
        - [전반적인 코드 품질/주의사항]

        # 주요 변경사항 상세 (중요하거나 핵심적인 파일만 작성)
        ## [클래스명/파일명]
        - 기능: [해당 파일 수정사항의 기능]
        - 변경: [핵심 로직 변경사항]
        - 잘한점, 고려해야할 점: [구현시 잘한점과 기존 코드와 중복되거나 고려해야할 점을 간략히]
        - 수정해야할 사항: [수정이 반드시 필요한 사항만 실제 코드를 포함해 작성]

        응답은 반드시 위의 JSON 형식을 준수해야 하며, HTML 내용은 실제 복사-붙여넣기가 가능해야 합니다.
        techStack 배열에는 코드에서 사용된 주요 기술들(SpringBoot, React 등)을 포함해주세요.
    """)

    portfolio_prompt = ChatPromptTemplate.from_template("""
    당신은 포트폴리오 작성을 돕는 전문가입니다. MR(Merge Request)의 내용을 분석하여 포트폴리오에 필요한 핵심 정보만을 간단명료하게 추출해주세요.

    주어진 MR 내용: {history}

    다음 형식으로 핵심 정보만 추출하여 응답해주세요:

    [기술 스택]
    - 사용된 핵심 기술만 나열 (프레임워크, 라이브러리, 도구 등)
    - 각 기술의 버전은 중요한 경우에만 표기

    [구현 기능]
    - bullet point로 3줄 이내 정리
    - 각 기능은 "~구현", "~개발" 형식으로 끝내기
    - 기술적으로 의미있는 내용만 포함

    [문제 해결]
    - 가장 중요한 기술적 도전/해결 1-2개만 선택
    - "문제: ~" / "해결: ~" 형식으로 작성
    - 실제 구현/해결한 내용만 포함 (개선해야할 사항 제외, 트러블 슈팅 내역으로)

    응답 시 주의사항:
    1. 모든 내용은 간단명료하게 작성
    2. 일반적이거나 당연한 내용은 제외
    3. 기술적으로 차별화된 내용만 포함
    4. 구체적인 기술/도구명 사용
    """)

    # 체인 구성
    review_chain = review_prompt | llm | StrOutputParser()
    portfolio_chain = portfolio_prompt | llm | StrOutputParser()
    final_review_chain = final_review_prompt | llm | StrOutputParser()

    try:
        for file_path, code_chunk, similar_codes in review_queries:

            try:
                review_result = chunked_review(projectId, llm, file_path, code_chunk, similar_codes, review_chain,
                                               code_review_memory)

                portfolio_memory.save_context(
                    {"input": f"Review for {file_path}"},
                    {"output": review_result}
                )
            except Exception as e:
                logger.info(f"개별 리뷰 중 오류 발생: {e}")
                continue

        portfolio_result = portfolio_chain.invoke({
            "input": "Generate final portfolio",
            "history": portfolio_memory.load_memory_variables({})[f"{projectId}_portfolio_{uuid}"]
        })

        code_review_result = final_review_chain.invoke({
            "input": "Generate final review",
            "history": code_review_memory.load_memory_variables({})[f"{projectId}_code_review_{uuid}"]
        }).replace('\n', '').replace('```html', '').replace('```', '').replace('json{', '{')

        try:
            # 2. 문자열을 JSON으로 파싱
            jsonData = json.loads(code_review_result)
            logger.info(jsonData)
            # 3. 파싱된 JSON 데이터 사용
            logger.info(jsonData['review'], "\n === \n", jsonData['techStack'])

            return re.sub(r'<title>.*?</title>', '', jsonData['review'].replace('\n', '')), portfolio_result, jsonData[
                'techStack']
        except json.JSONDecodeError as e:
            logger.info(f"JSON 파싱 에러: {e}")

    except Exception as e:
        logger.info(f"리뷰 중 오류 발생: {e}")
        return str(e)

    finally:
        portfolio_memory.clear()
        code_review_memory.clear()


def parse_git_diff(diff_string):
    # diff 헤더(@@ -0,0 +1,30 @@) 이후부터 파싱
    lines = diff_string.split('\n')
    start_idx = 0

    # diff 헤더 찾기
    for i, line in enumerate(lines):
        if line.startswith('@@'):
            start_idx = i + 1
            break

    removed_lines = []
    added_lines = []

    # 실제 코드 변경사항 파싱
    for line in lines[start_idx:]:
        # 빈 줄 무시
        if not line:
            continue

        # 삭제된 라인('-'로 시작)
        if line.startswith('-'):
            removed_lines.append(line[1:])  # '-' 제외하고 저장
        # 추가된 라인('+'로 시작)
        elif line.startswith('+'):
            added_lines.append(line[1:])  # '+' 제외하고 저장

    return removed_lines, added_lines


def chunked_review(project_id, llm, file_path: str, code_chunk: str, similar_codes: [], review_chain,
                   code_review_memory,
                   max_token_limit: int = 4000) -> str:
    uuid = generate_uuid()
    file_codeReview_memory = ConversationBufferMemory(
        memory_key=f"{project_id}_codereview_history_{uuid}",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 내용들로 코드리뷰가 가능하게 기능, 수정된 항목, 반드시 변경해야할 사항, 트러블 슈팅을 상세히 요약해"""
    )

    try:
        # 토큰 스플리터 설정
        splitter = TokenTextSplitter(
            chunk_size=max_token_limit // 2,
            chunk_overlap=100  # 문맥 유지를 위한 중복
        )

        # 코드 청크 분할
        code_chunks = splitter.split_text(code_chunk)

        # similar_codes를 문자열로 변환
        similar_codes_str = ""
        if similar_codes:
            for code in similar_codes:
                if isinstance(code, (list, tuple)):
                    similar_codes_str += "\n".join(str(c) for c in code)
                else:
                    similar_codes_str += str(code)
                similar_codes_str += "\n---\n"  # 각 코드 블록 구분

        # 각 코드 청크에 대해 리뷰 수행
        for i, chunk in enumerate(code_chunks):
            try:
                result = review_chain.invoke({
                    "file_path": f"{file_path} (Part {i + 1}/{len(code_chunks)})",
                    "code_chunk": chunk,
                    "similar_codes": similar_codes_str
                })

                file_codeReview_memory.save_context(
                    {"input": f"{file_path} (Part {i + 1}/{len(code_chunks)})"},
                    {"output": result}
                )

            except Exception as e:
                logger.info(f"청크 {i + 1} 처리 중 오류: {e}")
                continue

        # 리뷰 결과 통합
        if file_codeReview_memory:
            # 여러 리뷰 결과를 하나로 통합하는 프롬프트
            merge_prompt = ChatPromptTemplate.from_template("""
               다음은 하나의 파일에 대한 여러 부분의 리뷰 결과입니다.
               이들을 하나의 리뷰로 통합해 MR 전체의 코드리뷰 작성시 참고할 수 있게 해
    
               파일: {file_path}
               리뷰 결과들:
               {reviews}
           """)

            merge_chain = merge_prompt | llm | StrOutputParser()

            final_review = merge_chain.invoke({
                "file_path": file_path,
                "reviews": file_codeReview_memory.load_memory_variables({})[f"{project_id}_codereview_history_{uuid}"]
            })

            code_review_memory.save_context(
                {"input": f"{file_path}"},
                {"output": final_review}
            )

            return final_review
    except Exception as e:
        logger.info(f"코드리뷰 시 오류발생: {e}")
        return ''
    finally:
        file_codeReview_memory.clear()

    return "리뷰 결과가 없습니다."


def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')


def generate_advice(mr_summaries):
    """
    MR Summaries 데이터를 기반으로 LLM을 활용해 종합적인 조언 생성.
    Args:
        mr_summaries (list): MR 요약 데이터 리스트
    Returns:
        str: 전체 MR 요약을 기반으로 한 종합 조언
    """

    if not isinstance(mr_summaries, list):
        raise ValueError("mr_summaries는 리스트여야 합니다.")

    if not mr_summaries:
        return "MR Summaries 데이터가 비어 있습니다. 검토할 요약이 없습니다."

    # LLM 초기화
    llm_model = LLMModel()
    llm = llm_model.llm

    try:
        # MR Summaries를 하나의 텍스트로 병합
        combined_summaries = "\n".join([f"- {summary}" for summary in mr_summaries])

        # LLM 프롬프트 생성
        prompt = f"""
        다음은 여러 Merge Request의 요약 리스트입니다. 이를 기반으로 전체 프로젝트의 기술적 상태와 개선 방향에 대한 조언을 작성해주세요:

        Merge Request Summaries:
        {combined_summaries}

        작성 지침:
        1. 전체적인 기술적 상태를 분석
        2. 주요 개선 방향 제시 (3가지 이내)
        3. 잠재적 문제와 해결 방안 요약
        4. 기술 스택 최적화 또는 코드 품질 향상 방법 포함
        5. 간결하고 명확하게 작성
        """

        # LLM 호출 - 올바른 타입(str) 전달
        response = llm(prompt)
        logger.info(f"조언 생성 결과: {response.content}")
        # 결과 반환 AIMessage
        return response.content

    except Exception as e:
        logger.info(f"LLM을 사용한 조언 생성 중 오류 발생: {e}")
        raise RuntimeError("조언 생성 실패") from e
