# flaskProject/app/__init__.py
from flask import Flask
from dotenv import load_dotenv
import os
import logging

load_dotenv()  # ...env 파일에서 환경 변수 로드


def create_app():
    app = Flask(__name__)
    # app.config['OPENAI_API_KEY'] = os.getenv('OPENAI_API_KEY')
    app.config['MAX_TOKEN_LENGTH'] = os.getenv('MAX_TOKEN_LENGTH')
    app.logger.debug("Entered create_app() function.")

    # Flask 기본 로거 설정
    app.logger.setLevel(logging.INFO)  # DEBUG 레벨로 설정
    stream_handler = logging.StreamHandler()  # 콘솔 출력만 추가
    stream_handler.setFormatter(logging.Formatter("%(asctime)s [%(levelname)s] %(message)s"))
    app.logger.addHandler(stream_handler)

    from app.routes.routes import routes_bp
    app.register_blueprint(routes_bp)

    app.logger.debug("Finished create_app() function.")
    return app
