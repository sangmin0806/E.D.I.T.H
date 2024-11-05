from langchain_openai import ChatOpenAI
import os

class LLMModel:
    def __init__(self, model='gpt-4o-mini'):
        openai_api_key = os.getenv('OPENAI_API_KEY')
        self.llm = ChatOpenAI(
            model=model,
            temperature=0,
            openai_api_key=openai_api_key
        )
