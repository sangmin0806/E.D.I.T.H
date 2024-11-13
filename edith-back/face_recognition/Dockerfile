# # Python 이미지 사용
# FROM python:3.10.11-slim

# # 시스템 라이브러리 설치 (libGL, libgthread, sqlite3 등 포함)
# RUN apt-get update && \
#     apt-get install -y libgl1-mesa-glx libglib2.0-0 libgomp1 build-essential wget && \
#     apt-get install -y libsqlite3-dev && \
#     wget https://www.sqlite.org/2023/sqlite-autoconf-3410200.tar.gz && \
#     tar xzvf sqlite-autoconf-3410200.tar.gz && \
#     cd sqlite-autoconf-3410200 && \
#     ./configure && make && make install && \
#     rm -rf sqlite-autoconf-3410200*

# # 환경 변수 설정
# ENV LD_LIBRARY_PATH="/usr/local/lib:$LD_LIBRARY_PATH"

# # 작업 디렉터리 설정
# WORKDIR /app

# # requirements.txt 복사 및 패키지 설치
# COPY requirements.txt .
# RUN pip install --no-cache-dir -r requirements.txt

# BASE 이미지 사용
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

# FastAPI 애플리케이션 실행
CMD ["python", "main.py"]
