## 베이스 이미지 선택
#FROM python:3.10-slim
#
## 빌드에 필요한 기본 도구들 설치
#RUN apt-get update && apt-get install -y --no-install-recommends \
#    build-essential \
#    git \
#    && rm -rf /var/lib/apt/lists/*
#
## 작업 디렉터리 설정
#WORKDIR /app
#
## requirements.txt 복사
#COPY requirements.txt .
#
## 패키지 설치 (베이스 이미지에서 의존성만 설치)
#RUN pip install --no-cache-dir --upgrade pip && \
#    pip install --no-cache-dir torch --index-url https://download.pytorch.org/whl/cpu && \
#    pip install --no-cache-dir transformers chromadb langchain openai langchain-community \
#    langchain-openai python-dotenv sentence-transformers && \
#    pip install --no-cache-dir -r requirements.txt


# 기존 베이스 이미지를 사용
ARG ECR_URL
ARG ECR_REPO
ARG BASE_IMG_TAG

FROM ${ECR_URL}/${ECR_REPO}:${BASE_IMG_TAG} as base

# 최종 단계
FROM base

# 작업 디렉터리 설정
WORKDIR /app

# 나머지 파일 복사 (코드 파일만 복사하여 갱신)
COPY . .

# 로그 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/logs && chmod 777 /app/logs

# Flask 환경 변수 설정
ENV FLASK_APP=run.py
ENV FLASK_RUN_PORT=8083

# gunicorn 실행
CMD ["gunicorn", "--bind", "0.0.0.0:8083", "--workers=4", "--preload", "--timeout=180", "--log-level=debug", "--error-logfile=/app/logs/error.log", "--access-logfile=/app/logs/access.log", "run:app"]
