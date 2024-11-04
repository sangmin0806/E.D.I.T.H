# 베이스 이미지 선택
FROM python:3.10-slim

# 작업 디렉터리 설정
WORKDIR /app

# 필요한 파일들을 컨테이너로 복사
COPY . /app

# 필요한 라이브러리 설치
RUN pip install --no-cache-dir -r requirements.txt

# Flask 환경 변수 설정
ENV FLASK_APP=run.py
ENV FLASK_RUN_PORT=8083

# gunicorn을 사용하여 Flask 앱 실행
CMD ["gunicorn", "--bind", "0.0.0.0:8083", "run:app"]
