# codebert_model.py
from transformers import RobertaTokenizer, RobertaModel
import torch
from threading import Lock

tokenizer = None
model = None
model_lock = Lock()

def get_code_embedding(code_snippet):
    global tokenizer, model

    with model_lock:
        if tokenizer is None or model is None:
            tokenizer = RobertaTokenizer.from_pretrained('microsoft/graphcodebert-base')
            model = RobertaModel.from_pretrained('microsoft/graphcodebert-base')

    inputs = tokenizer(code_snippet, return_tensors="pt", truncation=True, max_length=512)
    with torch.no_grad():
        outputs = model(**inputs)
    embedding = outputs.last_hidden_state[:, 0, :].squeeze().numpy()
    return embedding
