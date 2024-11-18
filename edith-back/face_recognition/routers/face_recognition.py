import os
import logging
from fastapi import APIRouter, HTTPException, Response
from qdrant_client import QdrantClient
import httpx

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
SIMILARITY_THRESHOLD = 0.4


@match_router.post("/face-login")
async def face_recognition_login(vector: dict, response: Response):
    """
    클라이언트로부터 얼굴 벡터를 받아 유사도 검사 후 Spring 서버로 로그인 요청을 보냄.
    """
    image_vector = vector.get("vector")
    logger.info(f"클라이언트로부터 받은 벡터 데이터: {image_vector}")

    # Qdrant에서 가장 유사한 얼굴 찾기
    user_id, similarity_score = find_most_similar_face(image_vector)

    if user_id:
        if similarity_score <= SIMILARITY_THRESHOLD:
            # Spring 서버로 HTTP 요청을 보냄
            spring_response = await send_login_request_to_user_service(user_id)

            if "error" in spring_response:
                return {
                    "success": False,
                    "error": spring_response["error"],
                    "userId": user_id,
                    "similarity_score": similarity_score,
                }

            # Spring 서버에서 받은 응답 데이터 매핑
            response_data = spring_response.get("response", {})

            # FastAPI에서 쿠키 설정
            for key, value in spring_response.get("cookies", {}).items():
                response.set_cookie(key=key, value=value, httponly=True, samesite="None")

            return {
                "success": True,
                "response": {
                    "accessToken": response_data.get("accessToken"),
                    "refreshToken": response_data.get("refreshToken"),
                    "userId": response_data.get("userId"),
                    "username": response_data.get("username"),
                    "name": response_data.get("name"),
                    "email": response_data.get("email"),
                    "profileImageUrl": response_data.get("profileImageUrl"),
                    "similarity_score": similarity_score,
                },
            }

        # 유사도는 있지만 임계값보다 높음
        return {
            "success": False,
            "error": "유사도가 기준보다 낮아 로그인을 진행할 수 없습니다.",
            "userId": user_id,
            "similarity_score": similarity_score,
        }

    # Qdrant에서 유사한 얼굴을 찾지 못한 경우
    return {
        "success": False,
        "error": "유사한 얼굴을 찾을 수 없습니다.",
        "userId": None,
        "similarity_score": None,
    }


def find_most_similar_face(image_vector):
    """
    Qdrant에서 가장 유사한 얼굴을 검색.
    """
    search_result = qdrant_client.search(
        collection_name="user_embeddings",
        query_vector=image_vector,
        limit=1,
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
    userId를 Spring 서버로 전송하여 로그인 요청을 처리하고 응답을 반환.
    """
    url = "http://user-spring-boot-service:8181/api/v1/users/face-login"
    payload = {"userId": user_id}

    async with httpx.AsyncClient() as client:
        try:
            logger.info(f"Spring 서버에 로그인 요청 전송: userId={user_id}")
            response = await client.post(url, json=payload)
            response.raise_for_status()

            # Spring 서버 응답 처리
            response_json = response.json()

            # ApiResult 구조에 따라 `response` 필드 추출
            if not response_json.get("success"):
                logger.error(f"Spring 서버 요청 실패: {response_json}")
                return {"error": "Spring 서버 요청 실패"}

            # 쿠키와 응답 데이터 추출
            cookies = response.cookies
            response_data = response_json.get("response")  # `response` 필드 사용

            logger.info(f"Spring 서버에서 응답 수신: {response_data}")

            return {
                "response": response_data,
                "cookies": {cookie.name: cookie.value for cookie in cookies},
            }

        except httpx.HTTPStatusError as e:
            logger.error(f"Spring 서버 요청 실패: {e.response.status_code}")
            return {"error": "Spring 서버 요청 실패"}
        except Exception as e:
            logger.error("Spring 서버로 요청 중 오류 발생:", exc_info=True)
            return {"error": "Spring 서버로 요청 중 오류 발생"}
