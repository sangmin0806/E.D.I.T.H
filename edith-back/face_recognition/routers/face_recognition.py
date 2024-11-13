import os
from fastapi import  WebSocket, WebSocketDisconnect,APIRouter
from qdrant_client import QdrantClient

match_router = APIRouter()

# Qdrant 서버 정보 가져오기
qdrant_host = os.getenv("QDRANT_HOST", "qdrant.eks-work2.svc.cluster.local")
qdrant_port = os.getenv("QDRANT_PORT", "6333")

# Qdrant 클라이언트 초기화
qdrant_client = QdrantClient(host=qdrant_host, port=int(qdrant_port))

# 유사도 임계값 설정
SIMILARITY_THRESHOLD = 0.8

@match_router.websocket("/ws/users/face_recognition")
async def websocket_face_recognition(websocket: WebSocket):
    await websocket.accept()
    print("클라이언트 WebSocket 연결 성공")

    try:
        # WebSocket 연결 동안 클라이언트로부터 벡터 데이터 수신 및 얼굴 인식 처리
        while True:
            # 클라이언트에서 얼굴 벡터 데이터 수신
            data = await websocket.receive_json()
            image_vector = data.get("vector")  # 클라이언트가 전송한 벡터 값
            print(f"클라이언트로부터 받은 벡터 데이터: {image_vector}")

            # Qdrant에서 가장 유사한 얼굴 벡터 찾기
            user_id, similarity_score = find_most_similar_face(image_vector)

            # 유사도 점수가 임계값 이상인 경우에만 성공으로 처리
            if user_id and similarity_score >= SIMILARITY_THRESHOLD:
                await websocket.send_json({
                    "userId": user_id,
                    "similarity_score": similarity_score
                })
                await websocket.close()  # WebSocket 연결 종료
                break  # 유사한 얼굴이 발견되었으므로 while 루프 종료
            else:
                await websocket.send_text("유사한 얼굴을 찾을 수 없습니다.")

    except WebSocketDisconnect:
        print("클라이언트 WebSocket 연결이 끊어졌습니다.")
    except Exception as e:
        print("WebSocket 연결 종료 또는 오류:", e)
    finally:
        await websocket.close()

def find_most_similar_face(image_vector):
    """
    Qdrant에서 가장 유사한 얼굴을 찾는 함수.
    """
    search_result = qdrant_client.search(
        collection_name="user_embeddings",  # Qdrant에서 설정한 컬렉션 이름
        query_vector=image_vector,
        limit=1  # 가장 유사한 얼굴 한 개만 반환
    )

    if search_result:
        matched_face = search_result[0]
        user_id = matched_face.payload.get("user_id")  # Qdrant에서 저장된 userId
        similarity_score = matched_face.score  # 유사도 점수
        return user_id, similarity_score
    else:
        return None, None
