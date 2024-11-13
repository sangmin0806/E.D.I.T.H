import uuid
from langchain.memory import ConversationBufferMemory
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from app.services.llm_model import LLMModel


def estimate_tokens(text: str) -> int:
    return len(text) // 4


def merge_diffs(merge_requests, max_tokens: int = 3500):
    merged_requests = []
    current_batch = {
        'mrIds': [],
        'userId': None,
        'combined_diff': [],
        'estimated_tokens': 0
    }

    base_prompt_tokens = estimate_tokens("""
        너는 프로젝트 포트폴리오 제작 전문가야
        이는 MR의 파일별 git diff 야 
        포트폴리오 제작할 요약본을 만들어줘 이때 개발자 별로 구현한 내용을 정리해줘
        
        1. 기술 스택
        2. 핵심 기능
        * 있다면 3. 트러블 슈팅 내역
        """)

    for mr in merge_requests:
        mr_tokens = estimate_tokens(mr['diff'])

        if (not current_batch['userId'] or
                current_batch['userId'] != mr['userId'] or
                current_batch['estimated_tokens'] + mr_tokens + base_prompt_tokens > max_tokens):

            if current_batch['combined_diff']:
                merged_requests.append({
                    'mrIds': current_batch['mrIds'],
                    'userId': current_batch['userId'],
                    'diff': '\n---\n'.join(current_batch['combined_diff'])
                })
                current_batch = {
                    'mrIds': [],
                    'userId': mr['userId'],
                    'combined_diff': [],
                    'estimated_tokens': 0
                }

        current_batch['mrIds'].append(mr['mrId'])
        current_batch['userId'] = mr['userId']
        current_batch['combined_diff'].append(mr['diff'])
        current_batch['estimated_tokens'] += mr_tokens

    if current_batch['combined_diff']:
        merged_requests.append({
            'mrIds': current_batch['mrIds'],
            'userId': current_batch['userId'],
            'diff': '\n---\n'.join(current_batch['combined_diff'])
        })

    return merged_requests


def get_summary(llm, memory, merge_requests, summaries_dict) -> None:
    # 배치 크기 계산을 위한 기본 프롬프트 토큰 수 계산
    base_prompt = """너는 프로젝트 포트폴리오 제작 전문가야..."""
    base_tokens = estimate_tokens(base_prompt)
    current_batch = {
        'mrIds': [],
        'userId': None,
        'combined_diff': '',
        'total_tokens': base_tokens
    }
    cnt = 0
    for merge_request in merge_requests:
        print(cnt)
        print(merge_request)
        cnt += 1
        mr_id = merge_request['mrId']
        user_id = merge_request['userId']
        diff = merge_request['diff']

        # 현재 diff의 토큰 수 계산
        diff_tokens = estimate_tokens(diff)

        # 배치가 토큰 제한을 초과하는지 확인
        if current_batch['total_tokens'] + diff_tokens > 3500:  # 여유 공간 확보
            # 현재 배치 처리
            process_batch(llm, memory, current_batch, summaries_dict)
            # 새 배치 시작
            current_batch = {
                'mrIds': [],
                'userId': user_id,
                'combined_diff': '',
                'total_tokens': base_tokens
            }

        # 현재 MR을 배치에 추가
        current_batch['mrIds'].append(mr_id)
        current_batch['userId'] = user_id
        current_batch['combined_diff'] += f"\n---\n{diff}"
        current_batch['total_tokens'] += diff_tokens

    # 마지막 배치 처리
    if current_batch['mrIds']:
        process_batch(llm, memory, current_batch, summaries_dict)


def process_batch(llm, memory, batch, summaries_dict):
    summary_prompt = ChatPromptTemplate.from_template("""...""")
    summary_chain = summary_prompt | llm | StrOutputParser()

    try:
        if all(str(mr_id) in summaries_dict for mr_id in batch['mrIds']):
            # 기존 요약 사용
            combined_summary = "\n".join(summaries_dict[str(mr_id)]
                                         for mr_id in batch['mrIds'])
        else:
            # 새로운 요약 생성
            result = summary_chain.invoke({
                "user_id": batch['userId'],
                "diff": batch['combined_diff']
            })
            combined_summary = result

        # 메모리에 저장
        for mr_id in batch['mrIds']:
            memory.save_context(
                {"input": f"{mr_id}_{batch['userId']}"},
                {"output": combined_summary},
            )
    except Exception as e:
        print(f"배치 처리 중 오류 발생: {e}")


def get_portfolio(llm, memory, user_id, memory_key) -> str:
    portfolio_prompt = ChatPromptTemplate.from_template("""
                너는 프로젝트 포트폴리오 제작 전문가야
                해당 MR 요약본으로 개인화된 포트폴리오를 자세히 제작해줘 
                (HTML 형식으로 바로 붙여넣기 가능하게 완성해줘)

                개발자 ID : {user_id}

                ===요약본===
                {history}

                포트폴리오 내용 (해당 내용만 작성해):
                1. 프로젝트 설명
                2. 기술 스텍
                3. 핵심 로직
                4. 내가 구현한 내용
                5. 트러블 슈팅""")

    portfolio_chain = portfolio_prompt | llm | StrOutputParser()
    result = portfolio_chain.invoke({
        'user_id': user_id,
        'history': memory.load_memory_variables({})[f'{memory_key}']
    })

    return result


def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')


def make_portfolio(user_id, summaries, merge_requests) -> str:
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
        prompt="""해당 코드 리뷰 참고해 portfolio 만들 해당 MR 의 기술 스택, 핵심 로직, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )
    llm_model = LLMModel()
    llm = llm_model.llm

    try:
        # 3. 요약 생성 (토큰 제한을 고려한 배치 처리)
        get_summary(llm, portfolio_memory, merge_requests, summaries_dict)

        # 4. 최종 포트폴리오 생성
        result = get_portfolio(
            llm,
            portfolio_memory,
            user_id,
            f"portfolio_{uuid}"
        ).replace('```html', '').replace('```', '').replace('\n', '')

        return result

    except Exception as e:
        print(f"portfolio 생성시 에러 발생:{e}")
        return ''

    finally:
        portfolio_memory.clear()