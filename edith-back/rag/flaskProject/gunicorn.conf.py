import multiprocessing

# CPU 코어 수 * 2 + 1 권장
workers = multiprocessing.cpu_count() * 2 + 1

# Worker 당 쓰레드 수
threads = 2

# Worker 클래스 설정
# gthread: 쓰레드 기반 워커
worker_class = 'gthread'

# 바인딩할 주소와 포트
bind = '0.0.0.0:8000'

# Worker 타임아웃 설정
timeout = 120

# 로깅 설정
accesslog = './logs/access.log'
errorlog = './logs/error.log'
loglevel = 'info'

# Worker가 처리할 최대 요청 수
max_requests = 1000
max_requests_jitter = 50

# Graceful 재시작 타임아웃
graceful_timeout = 30

# 프로세스 이름
proc_name = 'flask-app'