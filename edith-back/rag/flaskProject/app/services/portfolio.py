### Portfolio 생성 로직

# 1. 특정 Branch 의 MR 기록을 모두 가져옴
# 2. MR ID 별로 반복
	# 2-1. 요약 존재 시 memory 에 SAVE
	# 2-2. 없을 경우 LLM 에 질의 -> MR 요약
# 3. 해당 Memory (질의 내역) 으로 최종 Portfolio 생성 -> 반환


# Repo Branch 의 MR 반환 함수
def get_mr(url, project_id, token, branch):
    return None

# 해당 MR 요약 반환 함수
def get_summary(mr, summaries):
    return None

# 최종 Portfolio 반환 함수
def get_portfolio(memory):
    return None


