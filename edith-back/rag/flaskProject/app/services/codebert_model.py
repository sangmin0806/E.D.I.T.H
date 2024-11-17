# codebert_model.py
from transformers import RobertaTokenizer, RobertaModel
import torch

# 모델과 토크나이저를 None으로 초기화
tokenizer = None
model = None

def get_code_embedding(code_snippet):
    global tokenizer, model

    # 모델과 토크나이저가 로드되지 않았다면 로드
    if tokenizer is None or model is None:
        tokenizer = RobertaTokenizer.from_pretrained('microsoft/graphcodebert-base')
        model = RobertaModel.from_pretrained('microsoft/graphcodebert-base')

    inputs = tokenizer(code_snippet, return_tensors="pt", truncation=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    embedding = outputs.last_hidden_state[:, 0, :].squeeze().numpy()
    return embedding
