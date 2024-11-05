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

# 해당 MR 요약 반환 함수
def get_summary(merge_request, summaries_dict):
    return None

# 최종 Portfolio 반환 함수
def get_portfolio(memory):
    return None

def make_portfolio(user_id, summaries, merge_requests):
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
        prompt="""해당 코드리뷰를 참고해 포트폴리오를 만들 해당 MR 의 기술스택, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )
    try:
        pass

    # 3. mr 반복하며 portfolio 생성

    # 4. 해당 기록을 바탕으로 최종 포트폴리오를 생성
    except Exception as e:
        print(f"포트폴리오 생성시 에러 발생:{e}")

    finally:
        portfolio_memory.clear()
    return None

def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')