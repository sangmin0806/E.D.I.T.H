import os


class Config:
    CHROMADB_URI = os.environ.get('CHROMADB_URI', "sqlite:///:memory:")  # 기본값은 메모리 저장소

