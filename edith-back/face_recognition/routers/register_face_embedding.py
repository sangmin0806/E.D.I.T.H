import os
from fastapi import FastAPI,APIRouter, HTTPException
from pydantic import BaseModel
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct, VectorParams
import jwt

app = FastAPI()

register_router = APIRouter()
# Qdrant 서버 정보 가져오기
qdrant_host = os.getenv("QDRANT_HOST", "qdrant.eks-work2.svc.cluster.local")
qdrant_port = os.getenv("QDRANT_PORT", "6333")

# Qdrant 클라이언트 초기화
qdrant_client = QdrantClient(host=qdrant_host, port=int(qdrant_port))

# 컬렉션 이름 및 설정
collection_name = "user_embeddings"
vector_size = 128  # 클라이언트에서 보낸 벡터 크기
distance = "Cosine"  # 유사도 계산 방식

# 컬렉션 생성 (존재하지 않는 경우)
try:
    qdrant_client.get_collection(collection_name)
except Exception:
    qdrant_client.recreate_collection(
        collection_name=collection_name,
        vectors_config=VectorParams(size=vector_size, distance=distance)
    )


class FaceEmbedding(BaseModel):
    user_id: str  # user_id 직접 전달받음
    embedding_vector: list[float]  # 클라이언트에서 전송한 벡터를 리스트로 받음

@app.post("/register-face")
async def register_face(data: FaceEmbedding):
    # 전달된 user_id와 벡터를 Qdrant에 저장
    try:
        point = PointStruct(id=data.user_id, vector=data.embedding_vector, payload={"user_id": data.user_id})
        qdrant_client.upsert(collection_name=collection_name, points=[point])
        return {"message": "얼굴 임베딩이 등록되었습니다."}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"임베딩 저장 실패: {str(e)}")
