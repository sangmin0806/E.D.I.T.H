import uuid
from langchain.memory import ConversationBufferMemory
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from app.services.llm_model import LLMModel
import time


def estimate_tokens(text: str) -> int:
    if not text:
        return 0

    # 한글 문자 세기 (한글은 문자당 약 2-3개 토큰)
    korean_chars = sum(1 for c in text if ord('가') <= ord(c) <= ord('힣'))
    korean_tokens = korean_chars * 2.5

    # 영문, 숫자, 기본 문장부호는 대략 4자당 1토큰
    basic_chars = sum(1 for c in text if c.isascii() and (c.isalnum() or c in '.,!? '))
    basic_tokens = basic_chars * 0.25

    # 특수문자, 코드 심볼은 대부분 1자당 1토큰에 가까움
    special_chars = len(text) - korean_chars - basic_chars
    special_tokens = special_chars

    # 줄바꿈은 보통 1토큰
    newlines = text.count('\n')

    total_tokens = int(korean_tokens + basic_tokens + special_tokens + newlines)
    return max(1, total_tokens)  # 최소 1토큰

def process_batch(llm, memory, batch, summaries_dict, description):
    summary_prompt = ChatPromptTemplate.from_template("""너는 프로젝트 포트폴리오 제작 전문가야
        이는 MR의 파일별 git diff 들이야 
        포트폴리오 제작할 요약본을 만들어 이때 개발자 별로 구현한 내용을 간략히 정리해줘
        (프로젝트 설명 : {description})
        
        개발자: {user_id}
        
        ===diff===
        {diff}
        
        * 전체적인 프로젝트에 관련한 내용
        1. 기술 스택
        2. 핵심 기능
        * 있다면 3. 트러블 슈팅 내역""")

    summary_chain = summary_prompt | llm | StrOutputParser()
    try:
        if all(str(mr_id) in summaries_dict for mr_id in batch['mrIds']):
            # 기존 요약 사용
            combined_summary = "\n".join(summaries_dict[str(mr_id)]
                                         for mr_id in batch['mrIds'])
        else:
            # 새로운 요약 생성
            result = summary_chain.invoke({
                "description": description,
                "user_id": batch['userId'],
                "diff": batch['diff']
            })
            combined_summary = result

        # 메모리에 저장
        memory.save_context(
            {"input": f"{batch['mrIds']}_{batch['userId']}"},
            {"output": combined_summary},
        )
    except Exception as e:
        print(f"배치 처리 중 오류 발생: {e}")


def get_portfolio(llm, memory, user_id, memory_key, description) -> str:
    portfolio_prompt = ChatPromptTemplate.from_template("""
                너는 프로젝트 포트폴리오 제작 전문가야
                해당 MR 요약본으로 포트폴리오를 *자세히 완성해 
                *HTML 형식, <title> 부터
                (프로젝트 설명 : AI 코드리뷰, 포트폴리오 자동 생성 프로젝트)
                
                개발자 ID : {user_id}
                설명: {description}

                ===요약본===
                {history}

                * 공통 포트폴리오 내용:
                1. 프로젝트 설명
                2. 기술 스텍
                3. 핵심 로직
                4. 트러블 슈팅
                
                * 내가 구현한 내용:
                1. 담당 파트
                2. 기술 스텍
                3. 트러블 슈팅 내역 
                """)

    portfolio_chain = portfolio_prompt | llm | StrOutputParser()
    result = portfolio_chain.invoke({
        'description': description,
        'user_id': user_id,
        'history': memory.load_memory_variables({})[f'{memory_key}']
    })

    return result


def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')

def merge_diffs(merge_requests, max_tokens: int = 10000):
    sorted_requests = sorted(merge_requests, key=lambda x: x['userId'])
    merged_requests = []
    current_batch = {
        'mrIds': [],
        'userId': None,
        'combined_diff': [],
        'estimated_tokens': 0
    }

    base_prompt_tokens = estimate_tokens("""
        너는 프로젝트 포트폴리오 제작 전문가야
        이는 MR의 파일별 git diff 들이야 
        포트폴리오 제작할 요약본을 만들어 이때 개발자 별로 구현한 내용을 간략히 정리해줘
        (프로젝트 설명 : AI 코드리뷰, 포트폴리오 자동 생성 프로젝트)

        개발자: {user_id}
        ===diff===
        {diff}
        1. 개발자
        2. 기술 스택
        3. 핵심 기능
        * 있다면 3. 트러블 슈팅 내역
        """)  # 프롬프트 토큰 수

    for mr in sorted_requests:
        mr_tokens = estimate_tokens(mr['diff'])

        # 새 배치가 필요한 경우
        if (not current_batch['userId'] or
                current_batch['userId'] != mr['userId'] or
                current_batch['estimated_tokens'] + mr_tokens + base_prompt_tokens > max_tokens):

            # 현재 배치가 있으면 저장
            if current_batch['combined_diff']:
                merged_requests.append({
                    'mrIds': current_batch['mrIds'],
                    'userId': current_batch['userId'],
                    'diff': '\n---\n'.join(current_batch['combined_diff'])
                })

            # 새 배치 시작
            current_batch = {
                'mrIds': [],
                'userId': mr['userId'],
                'combined_diff': [],
                'estimated_tokens': 0
            }

            # 새 배치에 현재 MR 추가
            current_batch['mrIds'].append(mr['mrId'])
            current_batch['userId'] = mr['userId']
            current_batch['combined_diff'].append(mr['diff'])
            current_batch['estimated_tokens'] = mr_tokens  # += 가 아닌 = 사용
        else:
            # 현재 배치에 MR 추가
            current_batch['mrIds'].append(mr['mrId'])
            current_batch['userId'] = mr['userId']
            current_batch['combined_diff'].append(mr['diff'])
            current_batch['estimated_tokens'] += mr_tokens

    # 마지막 배치 처리
    if current_batch['combined_diff']:
        merged_requests.append({
            'mrIds': current_batch['mrIds'],
            'userId': current_batch['userId'],
            'diff': '\n---\n'.join(current_batch['combined_diff'])
        })

    return merged_requests


def get_summary(llm, memory, merge_requests, summaries_dict, description) -> None:
    for batch in merge_requests:
        process_batch(llm, memory, batch, summaries_dict, description)


def make_portfolio(user_id, summaries, merge_requests, description) -> str:
    uuid = generate_uuid()
    # 1. summaries를 dictionary로 변환
    summaries_dict = {}
    if summaries and isinstance(summaries, list):
        summaries_dict = {
            summary['mrId']: summary['content']
            for summary in summaries
        }

    # 2. memory, llm 초기화
    portfolio_memory = ConversationBufferMemory(
        memory_key=f"portfolio_{uuid}",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 코드 리뷰 참고해 종합 portfolio 만들 요약본이야
        프로젝트 설명 : """ + description + """
        1. 프로젝트의 핵심 기능
        2. 기술스택
        3. 트러블 슈팅과 개발자별 구현한 내용 을 요약해"""
    )
    llm_model = LLMModel()
    llm = llm_model.llm

    try:
        # 3. merge_diffs를 사용하여 배치로 나누기
        merged_requests = merge_diffs(merge_requests)
        # 4. 요약 생성 (이미 배치로 나눠진 요청들 처리)
        get_summary(llm, portfolio_memory, merged_requests, summaries_dict, description)

        # 5. 최종 포트폴리오 생성
        result = get_portfolio(
            llm,
            portfolio_memory,
            user_id,
            f"portfolio_{uuid}",
            description
        ).replace('```html', '').replace('```', '').replace('\n', '')

        return result

    except Exception as e:
        print(f"portfolio 생성시 에러 발생:{e}")
        return ''

    finally:
        portfolio_memory.clear()