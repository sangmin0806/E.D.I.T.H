# reviewers.py
import os
from pathlib import Path
from app.chunking.GetCode import GitLabCodeChunker
from app.embeddings import CodeEmbeddingProcessor
from langchain_core.output_parsers import StrOutputParser
from langchain_openai import ChatOpenAI
from langchain.memory import ConversationBufferMemory
from langchain_core.prompts import ChatPromptTemplate
from langchain.text_splitter import TokenTextSplitter


def getCodeReview(url, token, projectId, branch, commits):
    # 0. DB 초기화
    vectorDB = CodeEmbeddingProcessor()

    # 1. git Clone
    chunker = GitLabCodeChunker(
        gitlab_url=url,
        gitlab_token=token,
        project_id=projectId,
        local_path='./cloneRepo/' + projectId,
        branch=branch
    )
    try:
        # 2. 파일별 임베딩
        project_path = chunker.clone_project()
        if not project_path:
            return ''

        # 3. 리뷰 할 코드들 메서드 Chunking
        file_chunks = []
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

                with open(file_path, 'r', encoding='utf-8') as f:
                    chunks = chunker.chunk_file(str(file_path), language)
                    file_chunks.extend(chunks)

        vectorDB.store_embeddings(file_chunks)

        review_queries = [] # path, diff (전문), 참고할 코드 (메서드)
        for commit in commits:
            language = get_language_from_extension(commit['new_path'])

            if (language == ''):
                continue
            removed_lines, added_lines = parse_git_diff(commit['diff'])
            similar_codes = []
            code_chunks = []

            # 코드 임베딩
            for added_line in added_lines:
                method = chunker.chunk_code(added_line, language)
                code_chunks.extend(method)
            # 유사도 분석
            for code_chunk in code_chunks:
                similar_codes.append(vectorDB.query_similar_code(code_chunk, 5)) # 여기여기==================
            review_queries.append([commit['new_path'], commit['diff'], similar_codes])

        # 5. 메서드 별 관련 코드 가져와 리트리버 생성, 질의
        openai_api_key = os.getenv('OPENAI_API_KEY')

        llm = ChatOpenAI(
            model="gpt-4o-mini",
            temperature=0,
            openai_api_key=openai_api_key
        )

        # 6. LLM 에 질의해 결과 반환
        result = get_code_review(projectId, review_queries, llm)

        # 7. 삭제
        chunker.cleanup_project_directory()
        return result

    except Exception as e:
        print(f"오류 발생: {e}")
        return ''

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


    portfolio_memory = ConversationBufferMemory(
        memory_key= projectId + "_portfolio",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 코드리뷰를 참고해 포트폴리오를 만들 해당 MR 의 기술스택, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )

    review_prompt = ChatPromptTemplate.from_template("""
        아래 git diff에 대해서만 코드 리뷰를 진행해주세요.
        중요한 부분, 핵심 로직은 강조해서 설명해주세요

        리뷰 대상 파일 경로: {file_path}

        ===리뷰 대상 git diff===
        {code_chunk}

        ===참고용 코드===
        {similar_codes}

        다음 항목에 대해 리뷰 대상 코드만 검토해 주세요:
        1. 기능 설명: 코드의 목적과 수행 기능, 구현 방법, 적용 기술등 상세히
        2. 개선 해야할 사항: 해당 언어/프레임워크의 관점에서 개선할 부분
        3. 수정 필요 항목: 버그나 오류 가능성이 있는 부분
        (4. 트러블 슈팅: 개선한 사항이 있으면 생성)""")

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

    code_review_result = ''

    try:
        for file_path, code_chunk, similar_codes in review_queries:
            try:
                review_result = chunked_review(projectId, llm, file_path, code_chunk, similar_codes, review_chain)

                portfolio_memory.save_context(
                    {"input": f"Review for {file_path}"},
                    {"output": review_result}
                )

                code_review_result += review_result + "\n ========================================= \n"

            except Exception as e:
                print(f"개별 리뷰 중 오류 발생: {e}")
                continue

        portfolio_result = portfolio_chain.invoke({
            "input": "Generate final review",
            "history": portfolio_memory.load_memory_variables({})[projectId + "_portfolio"]
        })

        print(portfolio_result)

        return code_review_result, portfolio_result

    except Exception as e:
        print(f"리뷰 중 오류 발생: {e}")
        return str(e)

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
                   max_token_limit: int = 4000) -> str:
    # 토큰 스플리터 설정
    splitter = TokenTextSplitter(
        chunk_size=max_token_limit // 2,
        chunk_overlap=100  # 문맥 유지를 위한 중복
    )

    # 코드 청크 분할
    code_chunks = splitter.split_text(code_chunk)
    similar_codes_str = []
    for code in similar_codes:
        if isinstance(code, (list, tuple)):
            similar_codes_str.extend(str(c) for c in code)
        else:
            similar_codes_str.append(str(code))

    similar_codes_combined = "\n\n===\n\n".join(similar_codes_str) if similar_codes_str else ""

    file_codeReview_memory = ConversationBufferMemory(
        memory_key=project_id + "_codereview_history",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 내용들로 코드리뷰가 가능하게 기능, 개선 사항, 수정 항목을 상세히 요약해"""
    )

    # 각 코드 청크에 대해 리뷰 수행
    for i, chunk in enumerate(code_chunks):
        try:
            result = review_chain.invoke({
                "file_path": f"{file_path} (Part {i + 1}/{len(code_chunks)})",
                "code_chunk": chunk,
                "similar_codes": similar_codes_combined
            })

            file_codeReview_memory.save_context(
                {"input": f"{file_path} (Part {i + 1}/{len(code_chunks)})"},
                {"output": result}
            )

        except Exception as e:
            print(f"청크 {i + 1} 처리 중 오류: {e}")
            continue

    # 리뷰 결과 통합
    if file_codeReview_memory:
        # 여러 리뷰 결과를 하나로 통합하는 프롬프트
        merge_prompt = ChatPromptTemplate.from_template("""
           다음은 하나의 파일에 대한 여러 부분의 리뷰 결과입니다.
           이들을 하나의 일관된 리뷰로 통합해주세요.

           파일: {file_path}
           리뷰 결과들:
           {reviews}

           통합된 리뷰를 작성해주세요.
       """)

        merge_chain = merge_prompt | llm | StrOutputParser()

        final_review = merge_chain.invoke({
            "file_path": file_path,
            "reviews": file_codeReview_memory.load_memory_variables({})[project_id + "_codereview_history"]
        })

        return final_review

    return "리뷰 결과가 없습니다."