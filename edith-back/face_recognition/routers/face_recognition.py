import os
import logging
from fastapi import WebSocket, WebSocketDisconnect, APIRouter
from qdrant_client import QdrantClient
import httpx  # HTTP 요청을 위한 라이브러리

# 로거 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

match_router = APIRouter()

# Qdrant 서버 정보 가져오기
qdrant_host = os.getenv("QDRANT_HOST", "qdrant.eks-work2.svc.cluster.local")
qdrant_port = os.getenv("QDRANT_PORT", "6333")

# Qdrant 클라이언트 초기화
qdrant_client = QdrantClient(host=qdrant_host, port=int(qdrant_port))

# 유사도 임계값 설정
SIMILARITY_THRESHOLD = 0.3

@match_router.websocket("/face-login")
async def websocket_face_recognition(websocket: WebSocket):
    await websocket.accept()
    logger.info("클라이언트 WebSocket 연결 성공")

    try:
        while True:
            data = await websocket.receive_json()
            image_vector = data.get("vector")
            logger.info(f"클라이언트로부터 받은 벡터 데이터: {image_vector}")

            user_id, similarity_score = find_most_similar_face(image_vector)

            if user_id and similarity_score <= SIMILARITY_THRESHOLD:
                # User 서버로 HTTP 요청을 보내고 응답을 받아옴
                success_response = await send_login_request_to_user_service(user_id)

                # User 서버로부터 받은 응답을 WebSocket으로 전달
                await websocket.send_json({
                    "userId": user_id,
                    "similarity_score": similarity_score,
                    "success": True,
                    "response": success_response
                })
                break  # 유사한 얼굴이 발견되었으므로 종료
            else:
                await websocket.send_json({
                    "userId": user_id,
                    "similarity_score": similarity_score,
                    "success": False
                })

    except WebSocketDisconnect:
        logger.info("클라이언트 WebSocket 연결이 끊어졌습니다.")
    except Exception as e:
        logger.error("WebSocket 연결 종료 또는 오류:", exc_info=True)
    finally:
        await websocket.close()

def find_most_similar_face(image_vector):
    search_result = qdrant_client.search(
        collection_name="user_embeddings",
        query_vector=image_vector,
        limit=1
    )

    if search_result:
        matched_face = search_result[0]
        user_id = matched_face.payload.get("user_id")
        similarity_score = matched_face.score
        return user_id, similarity_score
    else:
        return None, None

async def send_login_request_to_user_service(user_id: int):
    """
    userId를 User 서버로 전송하여 로그인 요청을 처리하고 응답을 반환.
    """
    url = "http://user-spring-boot-service:8181/api/v1/users/face-login"
    payload = {"userId": user_id}

    async with httpx.AsyncClient() as client:
        try:
            logger.info(f"User 서버에 로그인 요청 전: userId={user_id}")
            response = await client.post(url, json=payload)
            response.raise_for_status()
            logger.info(f"User 서버에 로그인 요청 성공: userId={user_id}")
            return response.json()  # User 서버의 응답 JSON 반환
        except httpx.HTTPStatusError as e:
            logger.error(f"User 서버 요청 실패: {e.response.status_code}")
            return {"error": "User 서버 요청 실패"}
        except Exception as e:
            logger.error("User 서버로 요청 중 오류 발생:", exc_info=True)
            return {"error": "User 서버로 요청 중 오류 발생"}
