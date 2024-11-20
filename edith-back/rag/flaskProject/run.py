import logging
from app import create_app

# 로깅 설정
# logging.basicConfig(
#     level=logging.DEBUG,  # 로그 레벨 설정 (DEBUG, INFO, WARNING, ERROR, CRITICAL)
#     format="%(asctime)s [%(levelname)s] %(message)s",  # 로그 포맷
#     handlers=[
#         logging.StreamHandler()  # 콘솔 출력
#     ]
# )
#
# logger = logging.getLogger(__name__)

app = create_app()

if __name__ == "__main__":
    logger.info("Starting Flask application on port 8083...")
    app.run(port=8083)
