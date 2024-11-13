### Portfolio 생성 로직

# 1. 특정 Branch 의 MR 기록을 모두 가져옴 -> X
# 2. MR ID 별로 반복
	# 2-1. 요약 존재 시 memory 에 SAVE
	# 2-2. 없을 경우 LLM 에 질의 -> MR 요약
# 3. 해당 Memory (질의 내역) 으로 최종 Portfolio 생성 -> 반환
from langchain.memory import ConversationBufferMemory
from app.services.llm_model import LLMModel
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
import uuid
import time

# 해당 MR 요약 반환 함수, -> Memory 에 저장만
def get_summary(llm, memory, merge_requests, summaries_dict) -> None:
    print(merge_requests)
    summary_prompt = ChatPromptTemplate.from_template("""
            너는 프로젝트 포트폴리오 제작 전문가야
            이는 MR의 파일별 git diff 야 
            포트폴리오 제작할 요약본을 만들어줘 이때 개발자 별로 구현한 내용을 정리해줘
            
            개발자: {user_id}
            파일 경로: {file_path}
            ===git diff===
            {diff}

            요약 내용:
            1. 기술 스택
            2. 핵심 기능
            (3. 트러블 슈팅: 개선한 사항이 있으면 생성)""")

    summary_chain = summary_prompt | llm | StrOutputParser()
    for merge_request in merge_requests:
        start_time = time.time()
        mr_id = merge_request['mrId']
        user_id = merge_request['userId']
        diff = merge_request['diff']
        file_path = merge_request['filePath']

        if summaries_dict and str(mr_id) in summaries_dict:
            # 요약본 존재 시 
            memory.save_context(
                {"input": f"{mr_id}_{user_id}"},
                {"output": f"{summaries_dict.get(mr_id)}"},
            )
        else:
            # LLM 에 요약
            result = summary_chain.invoke({
                "user_id": user_id,
                "file_path": file_path,
                "diff": diff
            })
            memory.save_context(
                {"input": f"{mr_id}_{user_id}"},
                {"output": result},
            )
        end_time = time.time()
        print(f"포폴 요약 1회 시간 {end_time - start_time}")
    return None

# 최종 Portfolio 반환 함수
def get_portfolio(llm, memory, user_id, memory_key) -> str:
    portfolio_prompt = ChatPromptTemplate.from_template("""
                너는 프로젝트 포트폴리오 제작 전문가야
                해당 MR 요약본으로 개인화된 포트폴리오를 제작해줘
                (평가 하지 말고 .md 형식으로 이모티콘까지 넣어 완성해줘)

                개발자: {user_id}
                
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

def make_portfolio(user_id, summaries, merge_requests) -> str:
    uuid = generate_uuid()
    # 1. summaries 를 dictionary 타입 으로 변환
    summaries_dict = {}
    if summaries and isinstance(summaries, list):
        summaries_dict = {
            summary['mrId']: summary['content']
            for summary in summaries
        }
    # 2. memory, llm 생성
    portfolio_memory = ConversationBufferMemory(
        memory_key=f"portfolio_{uuid}",
        max_token_limit=4000,
        return_messages=True,
        prompt="""해당 코드 리뷰 참고해 portfolio 만들 해당 MR 의 기술 스택, 핵심 로직, 트러블 슈팅 등을 기록할 수 있게 요약해"""
    )
    llm_model = LLMModel()
    llm = llm_model.llm
    try:
        start_time = time.time()
        # 3. mr 반복 하며 portfolio 생성
        get_summary(llm, portfolio_memory, merge_requests, summaries_dict)
        # 4. 해당 기록 기반 최종 portfolio 생성
        result = get_portfolio(llm, portfolio_memory, user_id, f"portfolio_{uuid}")

        return result

    except Exception as e:
        print(f"portfolio 생성시 에러 발생:{e}")

    finally:
        portfolio_memory.clear()
    return ''

def generate_uuid():
    return str(uuid.uuid4()).replace('-', '')