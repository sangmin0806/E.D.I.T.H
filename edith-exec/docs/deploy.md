# EDITH Deploy Manual

## 1. Docker

### Frontend
```
FROM node:18 AS build
WORKDIR /app
COPY package.json ./
RUN rm -rf node_modules/ .vite
RUN rm -rf node_modules
RUN npm install
COPY . .

RUN npm run build

FROM nginx:alpine
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]
```

### Backend

> spring server
```
FROM amazoncorretto:17
LABEL maintainer="minju"

RUN yum install -y glibc-langpack-ko
ENV LANG ko_KR.UTF8
ENV LC_ALL ko_KR.UTF8
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

VOLUME /tmp
ARG JAR_FILE=build/libs/<your-server>-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /<your-server>.jar

ENTRYPOINT ["java", \
 "-verbose:gc", \
 "-Xlog:gc*:stdout:time,uptime,level,tags", \
 "-Djava.security.egd=file:/dev/./urandom", \
 "-Dspring.profiles.active=prod", \
 "-jar", \
 "/user.jar", \
 "--server.port=<your-port>"]
```

> rag base image
```
FROM python:3.10-slim

# 빌드에 필요한 기본 도구들 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    git \
    && rm -rf /var/lib/apt/lists/*

# 작업 디렉터리 설정
WORKDIR /app

# requirements.txt 복사
COPY requirements.txt .

# 패키지 설치 (베이스 이미지에서 의존성만 설치)
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir torch --index-url https://download.pytorch.org/whl/cpu && \
    pip install --no-cache-dir transformers chromadb langchain openai langchain-community \
    langchain-openai python-dotenv sentence-transformers && \
    pip install --no-cache-dir -r requirements.txt

```

> rag server
```
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
ENV FLASK_RUN_PORT=<your-port>

# gunicorn 실행
CMD ["gunicorn", "--bind", "0.0.0.0:<your-port>", "--workers=4", "--timeout=180", "--log-level=info", "--error-logfile=/app/logs/error.log", "--access-logfile=/app/logs/access.log", "run:app"]
```
> face_recognition base image
```
 # Python 이미지 사용
 FROM python:3.10.11-slim

 # 시스템 라이브러리 설치 (libGL, libgthread, sqlite3 등 포함)
 RUN apt-get update && \
     apt-get install -y libgl1-mesa-glx libglib2.0-0 libgomp1 build-essential wget && \
     apt-get install -y libsqlite3-dev && \
     wget https://www.sqlite.org/2023/sqlite-autoconf-3410200.tar.gz && \
     tar xzvf sqlite-autoconf-3410200.tar.gz && \
     cd sqlite-autoconf-3410200 && \
     ./configure && make && make install && \
     rm -rf sqlite-autoconf-3410200*

 # 환경 변수 설정
 ENV LD_LIBRARY_PATH="/usr/local/lib:$LD_LIBRARY_PATH"

 # 작업 디렉터리 설정
 WORKDIR /app

 # requirements.txt 복사 및 패키지 설치
 COPY requirements.txt .
 RUN pip install --no-cache-dir -r requirements.txt

```
> face_recognition server
```
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

```

## 2. Jenkins

### Jenkins Credentials
| 유형                     | 키 이름                     | 값                                      | 설명                      | 상태   |
|--------------------------|-----------------------------|-----------------------------------------|---------------------------|--------|
| GitLab API token         | GitLab_Project_Access_Token | GitLab API token                       | GitLab API token          | Update |
| Username with password   | GitLab_ID_PW               | mj1584.mk@gmail.com/******             | Username with password    | Update |
| Username with password   | GitLab_Personal_Access_Token | mj1584.mk@gmail.com/******             | Username with password    | Update |
| SSH Username with private key | k11c206_SSH           | ubuntu                                 | SSH Username with private key | Update |
| Secret text              | k11c206_IP                | k11c206_IP                             | Secret text               | Update |
| Secret text              | SPRING_JWT_SECRET         | SPRING_JWT_SECRET                      | Secret text               |        |
| Username with password   | DOCKER_USER               | mj1584.mk@gmail.com/******             | Username with password    | Update |
| Username with password   | DOCKER_REPO               | mj1584/******                          | Username with password    | Update |
| Username with password   | GITHUB_USER               | mj1584.mk%40gmail.com/******           | Username with password    | Update |
| Username with password   | AWS_ACCESS_KEYS           | AKIAVFIWIW4YG7JKNFMU/******            | Username with password    | Update |
| Secret text              | ECR_URL                   | ECR_URL                                | Secret text               | Update |
| Secret text              | GITHUB_PAT                | personal_access_token                  | Secret text               | Update |
| Secret text              | OPENAI_API_KEY            | OPENAI_API_KEY                         | Secret text               | Update |
| Secret file              | OPENAI_API_KEY2           | .env                                   | Secret file               | Update |

### Jenkinsfile
> frontend-server
```
def skipRemainingStages = true

pipeline {
    agent any

    triggers {
        // GitLab Webhook을 통해 backend 브랜치로의 push 이벤트 시 트리거.
        gitlab(
            triggerOnPush: true,
        )
    }

    environment {
        REPO_URL = 'https://lab.ssafy.com/s11-final/S11P31C206.git'
        CHECKOUT_BRANCH = '*/develop'
        CHECKOUT_FOLDER = 'edith-front'
        SERVER_PORT = '3000'
        SERVER_NAME = 'frontend'
        DOCKER_USERINFO = credentials('DOCKER_USER')
        DOCKER_REPO = credentials('DOCKER_REPO')
        EC2_IP = credentials('k11c206_IP')
        SSH_INFO = credentials('k11c206_SSH')
    }

    stages {

        stage('Checkout Code') {
            steps {
                // backend 브랜치의 accompanyboard 폴더만 체크아웃
                checkout([$class: 'GitSCM',
                    branches: [[name: "${CHECKOUT_BRANCH}"]],
                    userRemoteConfigs: [[
                        url: "${REPO_URL}",
                        credentialsId: 'GitLab_Personal_Access_Token'
                    ]],
                    extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: "${CHECKOUT_FOLDER}/"]]]]
                ])
            }
        }

        stage('Build Docker Image') {
            when {
                changeset "${CHECKOUT_FOLDER}/**"
            }
            steps {
                script {
                    skipRemainingStages = false
                    sh """
                    docker build -t ${SERVER_NAME}:latest -f ${CHECKOUT_FOLDER}/Dockerfile ${CHECKOUT_FOLDER}
                    """
                }
            }
        }

        stage('Login to Docker Registry') {
            when {
                changeset "${CHECKOUT_FOLDER}/**"
            }
            steps {
                script {
                    sh """
                    docker login -u ${DOCKER_USERINFO_USR} -p ${DOCKER_USERINFO_PSW}
                    """
                }
            }
        }

        stage('Push Docker Image to Repository') {
            when {
                changeset "${CHECKOUT_FOLDER}/**"
            }
            steps {
                script {
                    sh """
                    docker tag ${SERVER_NAME}:latest ${DOCKER_REPO_USR}/${DOCKER_REPO_PSW}:${SERVER_NAME}-latest
                    docker push ${DOCKER_REPO_USR}/${DOCKER_REPO_PSW}:${SERVER_NAME}-latest
                    docker rmi ${SERVER_NAME}:latest || true
                    """
                }
            }
        }

        stage('Deploy to EC2') {
            when {
                changeset "${CHECKOUT_FOLDER}/**"
            }
            steps {
                script {
                    sh """
                    ssh -o StrictHostKeyChecking=no -i ${SSH_INFO} ${SSH_INFO_USR}@${EC2_IP} <<-EOF
                    docker stop ${SERVER_NAME} || true
                    docker rm ${SERVER_NAME} || true
                    docker rmi ${DOCKER_REPO_USR}/${DOCKER_REPO_PSW}:${SERVER_NAME}-latest || true
                    docker system prune -f --volumes
                    docker pull ${DOCKER_REPO_USR}/${DOCKER_REPO_PSW}:${SERVER_NAME}-latest
                    docker run -it -d --name ${SERVER_NAME} -p ${SERVER_PORT}:80 ${DOCKER_REPO_USR}/${DOCKER_REPO_PSW}:${SERVER_NAME}-latest
                    EOF
                    """.stripIndent()
                }
            }
        }

        stage('Logout from Docker Registry') {
            when {
                changeset "${CHECKOUT_FOLDER}/**"
            }
            steps {
                script {
                    sh """
                    docker logout
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
        success {
            script{
                if (skipRemainingStages) {
                    echo "No changes in ${SERVER_NAME} folder, skipping build and deploy."
                } else {
                    echo "Deployed successfully on port ${SERVER_PORT}!"
                    def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                    mattermostSend (
                        color: 'good',
                        message: "${user}님의 ${env.JOB_NAME} 서버 배포 성공. (#${env.BUILD_NUMBER}) ",
                    )
                }
            }
        }
        failure {
            echo 'Deployment failed!'
            script{
                def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                mattermostSend (
                    color: 'danger',
                    message: "${user}님? ${env.JOB_NAME} 서버 터졌는데요? (#${env.BUILD_NUMBER}) ",
                )
            }
        }
    }
}
```
> spring-server
```
def skipRemainingStages = true

pipeline {
    agent any

    triggers {
        gitlab(
            triggerOnPush: true,
        )
    }

    environment {

        // GitLab
        REPO_URL = 'https://lab.ssafy.com/s11-final/S11P31C206.git'
        CHECKOUT_BRANCH = '*/develop'
        CHECKOUT_FOLDER = 'edith-back'
        SERVER_PORT = '8081'
        SERVER_NAME = 'user'
        APP_TYPE = 'spring-boot-app'

        // AWS
        AWS_REGION = 'ap-northeast-2'
        AWS_ACCESS_KEYS = credentials('AWS_ACCESS_KEYS')
        ECR_URL = credentials('ECR_URL')
        ECR_REPO = 'ssafy/edith'

        // GITHUB
        GITHUB_REPO_URL = 'https://github.com/MJ-Kor/edith_eks_yaml.git'
        GITHUB_ID = 'mj1584.mk@gmail.com'
        GITHUB_NAME = 'MJ-Kor'

        // CI INFO
        PREVIOUS_BUILD_NUMBER = "${Integer.parseInt(env.BUILD_NUMBER) - 1}"
        CURRENT_BUILD_NUMBER = "${env.BUILD_NUMBER}"
        DELETE_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}"
        PREVIOUS_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}-${PREVIOUS_BUILD_NUMBER}"
        CURRENT_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}-${CURRENT_BUILD_NUMBER}"
        YAML_PATH = "${SERVER_NAME}.yaml"
        PROJECT_ROOT = 'project'
        YAML_ROOT = 'yaml'

        // DOCKER
        DOCKER_USERINFO = credentials('DOCKER_USER')
        DOCKER_REPO = credentials('DOCKER_REPO')

        // SPRING
        SPRING_JWT_SECRET = credentials('SPRING_JWT_SECRET')
    }

    stages {
        stage('Checkout Project Code') {
            steps {
                dir('project'){
                    checkout([$class: 'GitSCM',
                        branches: [[name: "${CHECKOUT_BRANCH}"]],
                        userRemoteConfigs: [[
                            url: "${REPO_URL}",
                            credentialsId: 'GitLab_Personal_Access_Token'
                        ]],
                        extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: "${SERVER_NAME}/"]]]]
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    skipRemainingStages = false
                    sh """
                    cd ${PROJECT_ROOT}/${CHECKOUT_FOLDER}/${SERVER_NAME}
                    chmod +x ./gradlew
                    ./gradlew clean build -x test
                    docker build -t ${SERVER_NAME}-${APP_TYPE}:latest --build-arg JAR_FILE=build/libs/${SERVER_NAME}-0.0.1-SNAPSHOT.jar .
                    docker tag ${SERVER_NAME}-${APP_TYPE}:latest ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    """
                }
            }
        }

        stage('Login to ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    sh """
                    aws configure set aws_access_key_id ${AWS_ACCESS_KEYS_USR}
                    aws configure set aws_secret_access_key ${AWS_ACCESS_KEYS_PSW}
                    aws configure set region ${AWS_REGION}
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URL}
                    """
                }
            }
        }

        stage('Delete Previous Docker Image from ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    // 1. AWS 명령어를 통해 이미지 태그들을 가져옴
                    def tags = sh(
                        script: "aws ecr list-images --repository-name ${ECR_REPO} --query 'imageIds[*].imageTag' --output text",
                        returnStdout: true
                    ).trim()

                    // 2. 가져온 태그들에서 특정 접두사로 시작하는 태그들만 필터링
                    def filteredTags = tags.tokenize().findAll { it.startsWith(DELETE_IMG_TAG) }

                    // 필터링된 태그 출력
                    echo "Filtered tags: ${filteredTags}"

                    // 3. 필터링된 태그가 있을 경우 삭제 진행
                    if (filteredTags) {
                        filteredTags.each { tag ->
                            echo "Deleting image with tag: ${tag}"
                            sh "aws ecr batch-delete-image --repository-name ${ECR_REPO} --image-ids imageTag=${tag}"
                        }
                    } else {
                        echo "No images found with prefix '${DELETE_IMG_TAG}', skipping deletion."
                    }
                }
            }
        }

        stage('Push Docker Image to ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    sh """
                    docker push ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    docker rmi ${SERVER_NAME}-${APP_TYPE}:latest || true
                    docker rmi ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    """
                }
            }
        }

        stage ('Get Yaml Repository') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                dir('yaml'){
                    git credentialsId: 'GITHUB_USER',
                    url: "${GITHUB_REPO_URL}",
                    branch: 'master'
                }
            }
        }
        stage('Setting .yaml File') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'GITHUB_USER', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_PASSWORD')]) {
                        sh """
                        git config --global user.email ${GITHUB_ID}
                        git config --global user.name ${GITHUB_NAME}
                        cd ${YAML_ROOT}/${SERVER_NAME}
                        sed -i 's|image: \\(.*\\):[^ ]*|image: \\1:${CURRENT_IMG_TAG}|' ${YAML_PATH}
                        git add .
                        git commit -m 'fix: Update image tag to ${CURRENT_IMG_TAG}'
                        git push https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_REPO_URL.replace('https://','')}
                        """
                    }
                }
            }
        }

        stage('Logout from ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps{
                script {
                    sh """
                    docker logout ${ECR_URL}
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
        success {
            script{
                if (skipRemainingStages) {
                    echo "No changes in ${SERVER_NAME} folder, skipping build and deploy."
                } else {
                    echo "CI successfully on ${SERVER_NAME} server!"
                    def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                    mattermostSend (
                        color: 'good',
                        message: "${user}님의 ${env.JOB_NAME} 서버 CI 성공. (#${env.BUILD_NUMBER}) ",
                    )
                }
            }
        }
        failure {
            echo 'CI failed!'
            script{
                def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                mattermostSend (
                    color: 'danger',
                    message: "${user}님? ${env.JOB_NAME} 서버 CI 실패했는데요? (#${env.BUILD_NUMBER}) ",
                )
            }
        }
    }
}
```
> flask, fast-api server
```
def skipRemainingStages = true

pipeline {
    agent any

    triggers {
        gitlab(
            triggerOnPush: true,
        )
    }

    environment {

        // GitLab
        REPO_URL = 'https://lab.ssafy.com/s11-final/S11P31C206.git'
        CHECKOUT_BRANCH = '*/develop'
        CHECKOUT_FOLDER = 'edith-back'
        SERVER_PORT = '8083'
        SERVER_NAME = 'rag'
        APP_TYPE = 'flask-app'

        // AWS
        AWS_REGION = 'ap-northeast-2'
        AWS_ACCESS_KEYS = credentials('AWS_ACCESS_KEYS')
        ECR_URL = credentials('ECR_URL')
        ECR_REPO = 'ssafy/edith'

        // GITHUB
        GITHUB_REPO_URL = 'https://github.com/MJ-Kor/edith_eks_yaml.git'
        GITHUB_ID = 'mj1584.mk@gmail.com'
        GITHUB_NAME = 'MJ-Kor'

        // CI INFO
        PREVIOUS_BUILD_NUMBER = "${Integer.parseInt(env.BUILD_NUMBER) - 1}"
        CURRENT_BUILD_NUMBER = "${env.BUILD_NUMBER}"
        DELETE_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}"
        PREVIOUS_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}-${PREVIOUS_BUILD_NUMBER}"
        CURRENT_IMG_TAG = "${SERVER_NAME}-${APP_TYPE}-${CURRENT_BUILD_NUMBER}"
        BASE_IMG_TAG = "${SERVER_NAME}-flask-base"
        YAML_PATH = "${SERVER_NAME}.yaml"
        PROJECT_ROOT = 'project'
        YAML_ROOT = 'yaml'

        // DOCKER
        DOCKER_USERINFO = credentials('DOCKER_USER')
        DOCKER_REPO = credentials('DOCKER_REPO')

        // SPRING
        SPRING_JWT_SECRET = credentials('SPRING_JWT_SECRET')
    }

    stages {
        stage('Checkout Project Code') {
            steps {
                dir('project'){
                    checkout([$class: 'GitSCM',
                        branches: [[name: "${CHECKOUT_BRANCH}"]],
                        userRemoteConfigs: [[
                            url: "${REPO_URL}",
                            credentialsId: 'GitLab_Personal_Access_Token'
                        ]],
                        extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths: [[path: "${SERVER_NAME}/"]]]]
                    ])
                }
            }
        }

        stage('Login to ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    sh """
                    aws configure set aws_access_key_id ${AWS_ACCESS_KEYS_USR}
                    aws configure set aws_secret_access_key ${AWS_ACCESS_KEYS_PSW}
                    aws configure set region ${AWS_REGION}
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URL}
                    """
                }
            }
        }

        stage('Build Docker Image') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    skipRemainingStages = false
                    sh """
                    cd ${PROJECT_ROOT}/${CHECKOUT_FOLDER}/${SERVER_NAME}/flaskProject
                    docker pull ${ECR_URL}/${ECR_REPO}:${BASE_IMG_TAG}
                    docker build --build-arg ECR_URL=${ECR_URL} --build-arg ECR_REPO=${ECR_REPO} --build-arg BASE_IMG_TAG=${BASE_IMG_TAG} -t ${SERVER_NAME}-${APP_TYPE}:latest .
                    docker tag ${SERVER_NAME}-${APP_TYPE}:latest ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    """
                }
            }
        }

        stage('Delete Previous Docker Image from ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    // 1. AWS 명령어를 통해 이미지 태그들을 가져옴
                    def tags = sh(
                        script: "aws ecr list-images --repository-name ${ECR_REPO} --query 'imageIds[*].imageTag' --output text",
                        returnStdout: true
                    ).trim()

                    // 2. 가져온 태그들에서 특정 접두사로 시작하는 태그들만 필터링
                    def filteredTags = tags.tokenize().findAll { it.startsWith(DELETE_IMG_TAG) }

                    // 필터링된 태그 출력
                    echo "Filtered tags: ${filteredTags}"

                    // 3. 필터링된 태그가 있을 경우 삭제 진행
                    if (filteredTags) {
                        filteredTags.each { tag ->
                            echo "Deleting image with tag: ${tag}"
                            sh "aws ecr batch-delete-image --repository-name ${ECR_REPO} --image-ids imageTag=${tag}"
                        }
                    } else {
                        echo "No images found with prefix '${DELETE_IMG_TAG}', skipping deletion."
                    }
                }
            }
        }

        stage('Push Docker Image to ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    sh """
                    docker push ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    docker rmi ${SERVER_NAME}-${APP_TYPE}:latest || true
                    docker rmi ${ECR_URL}/${ECR_REPO}:${CURRENT_IMG_TAG}
                    """
                }
            }
        }

        stage ('Get Yaml Repository') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                dir('yaml'){
                    git credentialsId: 'GITHUB_USER',
                    url: "${GITHUB_REPO_URL}",
                    branch: 'master'
                }
            }
        }
        stage('Setting .yaml File') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'GITHUB_USER', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_PASSWORD')]) {
                        sh """
                        git config --global user.email ${GITHUB_ID}
                        git config --global user.name ${GITHUB_NAME}
                        cd ${YAML_ROOT}/${SERVER_NAME}
                        sed -i 's|image: \\(.*\\):[^ ]*|image: \\1:${CURRENT_IMG_TAG}|' ${YAML_PATH}
                        git add .
                        git commit -m 'fix: Update image tag to ${CURRENT_IMG_TAG}'
                        git push https://${GITHUB_USERNAME}:${GITHUB_PASSWORD}@${GITHUB_REPO_URL.replace('https://','')}
                        """
                    }
                }
            }
        }

        stage('Logout from ECR') {
            when {
                changeset "${CHECKOUT_FOLDER}/${SERVER_NAME}/**"
            }
            steps{
                script {
                    sh """
                    docker logout ${ECR_URL}
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
        success {
            script{
                if (skipRemainingStages) {
                    echo "No changes in ${SERVER_NAME} folder, skipping build and deploy."
                } else {
                    echo "CI successfully on ${SERVER_NAME} server!"
                    def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                    mattermostSend (
                        color: 'good',
                        message: "${user}님의 ${env.JOB_NAME} 서버 CI 성공. (#${env.BUILD_NUMBER}) ",
                    )
                }
            }
        }
        failure {
            echo 'CI failed!'
            script{
                def user = sh(script: 'git log -1 --pretty=format:"%an"', returnStdout: true).trim()
                mattermostSend (
                    color: 'danger',
                    message: "${user}님? ${env.JOB_NAME} 서버 CI 실패했는데요? (#${env.BUILD_NUMBER}) ",
                )
            }
        }
    }
}
```

## 3. Nginx
- EKS 폴더에서 확인