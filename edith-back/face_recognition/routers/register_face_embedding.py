import os
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct, VectorParams

register_router = APIRouter()
# Qdrant 서버 정보 가져오기
qdrant_host = os.getenv("QDRANT_HOST", "qdrant.eks-work2.svc.cluster.local")
qdrant_port = os.getenv("QDRANT_PORT", "6333")

# Qdrant 클라이언트 초기화
qdrant_client = QdrantClient(host=qdrant_host, port=int(qdrant_port))

# 컬렉션 이름 및 설정
collection_name = "user_embeddings"
vector_size = 128  # 클라이언트에서 보낸 벡터 크기
distance = "Euclid"  # 유사도 계산 방식
 
# 컬렉션 생성 (존재하지 않는 경우)
try:
    qdrant_client.get_collection(collection_name)
except Exception:
    qdrant_client.create_collection(
        collection_name=collection_name,
        vectors_config=VectorParams(size=vector_size, distance=distance)
    )

class FaceEmbedding(BaseModel):
    userId: int   # user_id 직접 전달받음
    embeddingVectors: list[list[float]]  # 클라이언트에서 전송한 벡터를 리스트로 받음

@register_router.post("/register-face")
async def register_face(data: FaceEmbedding):
    try:
        # 벡터 리스트 전체를 payload로 저장
        point = PointStruct(
            id=data.userId,
            vector=data.embeddingVectors[0],  # 첫 번째 벡터를 `vector` 필드에 사용 (필수로 하나가 있어야 함)
            payload={
                "user_id": data.userId,
                "embeddingVectors": data.embeddingVectors  # 2차원 리스트 전체를 payload로 추가
            }
        )

        # Qdrant에 단일 포인트로 업로드
        qdrant_client.upsert(collection_name=collection_name, points=[point])

        return {"message": "2차원 벡터 리스트가 userId와 매핑되어 저장되었습니다."}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"임베딩 저장 실패: {str(e)}")
