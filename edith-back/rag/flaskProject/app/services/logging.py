import logging


def __get_logger():
    """로거 인스턴스 반환
    """
    __logger = logging.getLogger('logger')

    # 로그 포멧 정의
    formatter = logging.Formatter(
        'RAG_LOG##%(levelname)s##%(asctime)s##%(filename)s::%(lineno)s>>%(message)s'
    )

    # 스트림 핸들러 정의
    stream_handler = logging.StreamHandler()
    # 각 핸들러에 포멧 지정
    stream_handler.setFormatter(formatter)
    # 로거 인스턴스에 핸들러 삽입
    __logger.addHandler(stream_handler)
    # 로그 레벨 정의
    __logger.setLevel(logging.DEBUG)

    return __logger