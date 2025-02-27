import uvicorn
from fastapi import FastAPI
from routers.register_face_embedding import register_router
from routers.face_recognition import match_router

app = FastAPI()

# 얼굴 등록과 얼굴 일치 확인 라우터 등록
app.include_router(register_router, prefix="/api/v1/face-recognition")
app.include_router(match_router,prefix="/api/v1/face-recognition")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8084)
