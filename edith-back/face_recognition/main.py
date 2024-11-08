import uvicorn
from fastapi import FastAPI
from register_face_embedding import face_recognition_router as register_router
from face_recognition import face_recognition_router as match_router

app = FastAPI()

# 얼굴 등록과 얼굴 일치 확인 라우터 등록
app.include_router(register_router,prefix="/face-recognition")
app.include_router(match_router,prefix="/face-recognition")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
