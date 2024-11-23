from langchain_openai import ChatOpenAI
import os
import time

class LLMModel:
    def __init__(self, model='gpt-4o-mini'):
        # 키를 배열로 로드
        self.api_keys = os.getenv('OPENAI_API_KEY').split(',')
        self.current_key_index = 0
        self.model = model
        self.temperature = 0
        self.llm = self._initialize_llm(self.api_keys[self.current_key_index])

    def _initialize_llm(self, api_key):
        return ChatOpenAI(
            model=self.model,
            temperature=self.temperature,
            openai_api_key=api_key.strip()
        )

    def _rotate_key(self):
        self.current_key_index = (self.current_key_index + 1) % len(self.api_keys)
        new_key = self.api_keys[self.current_key_index].strip()
        print(f"Switching to API key: {new_key[:10]}...")  # 일부만 출력해 보안 유지
        self.llm = self._initialize_llm(new_key)

    def request(self, **kwargs):
        while True:
            try:
                # LLM 호출
                return self.llm(**kwargs)
            except Exception as e:
                error_message = str(e)
                if "rate limit" in error_message or "TPM" in error_message:
                    print(f"Rate limit reached for key: {self.api_keys[self.current_key_index][:10]}...")
                    self._rotate_key()
                    time.sleep(2)  # 키 교체 후 잠시 대기
                else:
                    raise e
