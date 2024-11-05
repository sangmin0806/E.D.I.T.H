### Portfolio 생성 로직

# 1. 특정 Branch 의 MR 기록을 모두 가져옴 -> X
# 2. MR ID 별로 반복
	# 2-1. 요약 존재 시 memory 에 SAVE
	# 2-2. 없을 경우 LLM 에 질의 -> MR 요약
# 3. 해당 Memory (질의 내역) 으로 최종 Portfolio 생성 -> 반환
from langchain.memory import ConversationBufferMemory
import uuid
# # Repo Branch 의 MR 반환 함수
# def get_mr(url, project_id, token, branch):
#     return None

# 해당 MR 요약 반환 함수, -> Memory 에 저장만
def get_summary(llm, memory, merge_requests, summaries_dict) -> None:
    for merge_request in merge_requests:
        mr_id = merge_request['mrId']
        user_id = merge_request['userId']
        diff = merge_request['diff']
        if mr_id in summaries_dict:
            memory.save_context(
                {"input": f"{mr_id}_{user_id}"},
                {"output": f"{summaries_dict.get(mr_id)}"},
            )
        else:
            # LLM 에다가 내놔 시전
            pass
            
    return None

# 최종 Portfolio 반환 함수
def get_portfolio(llm, memory) -> str:

    return ''

def make_portfolio(user_id, summaries, merge_requests) -> str:
    # 1. summaries 를 dictionary 타입 으로 변환
    summaries_dict = {
        summary['mrId']: summary['content']
        for summary in summaries
    }
    # 2. memory 생성
    portfolio_memory = ConversationBufferMemory(
        memory_key="portfolio",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 코드 리뷰 참고해 portfolio 만들 해당 MR 의 기술 스택, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )
    try:
        # 3. mr 반복 하며 portfolio 생성
        get_summary('', portfolio_memory, merge_requests, summaries_dict)
        # 4. 해당 기록 기반 최종 portfolio 생성
        result = get_portfolio('', portfolio_memory)

        return result

    except Exception as e:
        print(f"portfolio 생성시 에러 발생:{e}")

    finally:
        portfolio_memory.clear()
        return ''

def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')