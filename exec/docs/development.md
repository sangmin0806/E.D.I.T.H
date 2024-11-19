# EDITH Development Manual

## 1. 사용 도구
- 이슈 관리: JIRA
- 형상 관리: GitLab, Github
- 커뮤니케이션: Notion, MatterMost
- 디자인: Figma, Canva
- 클라우드 인프라: AWS EC2, AWS EKS, Docker
- CI/CD: Jenkins, ArgoCD
- 모니터링및 로깅: Grafana, Loki

## 2. 개발 도구
- Visual Studio Code
- IntelliJ
- PyCharm

## 3. 개발 환경

### Frontend
|Node.js| 20.14.10 | 
|:---:|:---:|
|React  | 18.3.1 |
|Tailwind CSS  | 3.4.14 |
|Zustand  | 4.5.4 |

### Backend
|Java| OpenJDK 17 | 
|:---:|:---:|
|Spring Boot  | 3.3.5 |
|Spring Security  | 6.3.4 |
|Spring Cloud Gateway  | 4.1.5 |
| Gradle | 8.10.2 |

### DB
|||
|:---:|:---:|
|MySQL  | 8.0.33 |
|Redis  | 4.3.1 |
|Qdrant  | 1.1.0 |
|ChromaDB  | 0.5.18 |

### AI
|Python| 3.9 | 
|:---:|:---:|
|Flask  | 3.0.3 |
|FastAPI| 0.115.4 |
|Langchain  | 0.3.7 |
|OpenAI  | 1.54.3 |

### Infra
|Jenkins| 2.482 | 
|:---:|:---:|
|ArgoCD| 2.13.0 |
|Docker| 27.3.1 |
|Grafana| 10.3.3 |
|Grafana-Loki| 2.6.1 |
|Grafana-Promtail| 2.9.3 |
|Nginx| Stable Alphine |
|AWS EKS| 1.30.2 |
|AWS ECR| - |
|AWS EC2| t3.xlarge, t3.large |

## 4. 외부 서비스
- OpenAI API

## 5. 환경 변수


```
# Back-end(user)

spring:
  application:
    name: user
  datasource:
    url: jdbc:mysql://user-mysql-clusterip.eks-work2.svc.cluster.local:3306/user_database
    username: edith
    password: ssafy
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

  jwt:
    secret: ${SPRING_JWT_SECRET}
    access-token-expiration: 3600000
    refresh-token-expiration: 86400000
  data:
    redis:
      host: redis-clusterip.eks-work2.svc.cluster.local
      port: 6379

server:
  port: 8081
app:
  cookie:
    expiration: 3600

fastapi:
  url: http://face-recognition-fastapi-service:8184

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health, info
```
```
# Back-end (scg)

frontend:
  servers:
    - "http://localhost:5173"
    - "http://k11c206.p.ssafy.io:3000"

spring:
  application:
    name: SCG
  jwt:
    secret: ${SPRING_JWT_SECRET}
  cloud:
    gateway:
      httpclient:
        connect-timeout: 600000
        response-timeout: 600000
      routes:
        - id: user-test
          uri: http://user-spring-boot-service:8181
          predicates:
            - Path=/api/v1/users/test
          filters:
            - name: JwtAuthFilter

        - id: user-validate
          uri: http://user-spring-boot-service:8181
          predicates:
            - Path=/api/v1/users/validate
          filters:
            - name: JwtAuthFilter

        - id: user-api
          uri: http://user-spring-boot-service:8181
          predicates:
            - Path=/api/v1/users/**

        - id: developmentassistant-projects-api
          uri: http://developmentassistant-spring-boot-service:8182
          predicates:
            - Path=/api/v1/projects/**

        - id: developmentassistant-portfolio-api
          uri: http://developmentassistant-spring-boot-service:8182
          predicates:
            - Path=/api/v1/portfolio/**

        - id: developmentassistant-webhook-api
          uri: http://developmentassistant-spring-boot-service:8182
          predicates:
            - Path=/api/v1/webhook/**

        - id: face-recognition-api
          uri: http://face-recognition-fastapi-service:8184
          predicates:
            - Path=/api/v1/face-recognition/**

        - id: face-recognition-websocket
          uri: ws://face-recognition-fastapi-service:8184
          predicates:
            - Path=/ws/v1/face-recognition/face-login

        - id: kubetest
          uri: http://kubetest:9190
          predicates:
            - Path=/test
server:
  port: 8080

logging:
  level:
    org.springframework.cloud.gateway: debug

management:
  endpoint:
    gateway:
      enabled: true
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health, info, gateway
```
```
# Back-end (developmentassistant)

spring:
  application:
    name: developmentassistant
  datasource:
    url: jdbc:mysql://developmentassistant-mysql-clusterip.eks-work2.svc.cluster.local:3306/developmentassistant_database
    username: edith
    password: ssafy
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
  data:
    redis:
      host: redis-clusterip.eks-work2.svc.cluster.local
      port: 6379
server:
  port: 8082

```

## 6. Dependency

### user 서버 Dependency
| Group ID                      | Artifact ID                        | Version         |
|-------------------------------|------------------------------------|-----------------|
| org.projectlombok             | lombok                             | 1.18.34         |
| io.jsonwebtoken               | jjwt-api                           | 0.12.3          |
| io.jsonwebtoken               | jjwt-impl                          | 0.12.3          |
| io.jsonwebtoken               | jjwt-jackson                       | 0.12.3          |
| com.fasterxml.jackson.core    | jackson-databind                   | 2.17.2          |
| com.fasterxml.jackson.core    | jackson-annotations                | 2.17.2          |
| com.fasterxml.jackson.core    | jackson-core                       | 2.17.2          |
| com.fasterxml.jackson.datatype | jackson-datatype-jdk8             | 2.17.2          |
| com.fasterxml.jackson.datatype | jackson-datatype-jsr310           | 2.17.2          |
| com.fasterxml.jackson.module  | jackson-module-parameter-names     | 2.17.2          |
| org.springframework.boot     | spring-boot-starter-data-jdbc      | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-jdbc           | 3.3.5           |
| org.springframework.boot     | spring-boot-starter                | 3.3.5           |
| org.springframework.boot     | spring-boot                        | 3.3.5           |
| org.springframework          | spring-core                        | 6.1.14          |
| org.springframework          | spring-context                     | 6.1.14          |
| org.springframework          | spring-aop                         | 6.1.14          |
| org.springframework          | spring-beans                       | 6.1.14          |
| org.springframework          | spring-expression                  | 6.1.14          |
| org.springframework.boot     | spring-boot-autoconfigure          | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-logging        | 3.3.5           |
| ch.qos.logback                | logback-classic                    | 1.5.11          |
| ch.qos.logback                | logback-core                       | 1.5.11          |
| org.slf4j                     | slf4j-api                          | 2.0.16          |
| org.apache.logging.log4j      | log4j-to-slf4j                     | 2.23.1          |
| org.apache.logging.log4j      | log4j-api                          | 2.23.1          |
| org.springframework.data      | spring-data-jdbc                   | 3.3.5           |
| org.springframework.data      | spring-data-relational             | 3.3.5           |
| org.springframework.data      | spring-data-commons                | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-data-jpa       | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-aop            | 3.3.5           |
| org.aspectj                   | aspectjweaver                      | 1.9.22.1        |
| org.hibernate.orm             | hibernate-core                     | 6.5.3.Final     |
| jakarta.persistence          | jakarta.persistence-api            | 3.1.0           |
| jakarta.transaction          | jakarta.transaction-api            | 2.0.1           |
| org.springframework.data      | spring-data-jpa                    | 3.3.5           |
| org.springframework          | spring-orm                         | 6.1.14          |
| org.antlr                     | antlr4-runtime                     | 4.13.0          |
| org.springframework.boot     | spring-boot-starter-security       | 3.3.5           |
| org.springframework.security  | spring-security-config            | 6.3.4           |
| org.springframework.security  | spring-security-core              | 6.3.4           |
| org.springframework.security  | spring-security-web               | 6.3.4           |
| org.springframework.boot     | spring-boot-starter-web            | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-json           | 3.3.5           |
| org.springframework.boot     | spring-boot-starter-tomcat         | 3.3.5           |
| jakarta.annotation           | jakarta.annotation-api             | 2.1.1           |
| org.apache.tomcat.embed       | tomcat-embed-core                  | 10.1.31         |
| org.apache.tomcat.embed       | tomcat-embed-el                    | 10.1.31         |
| org.apache.tomcat.embed       | tomcat-embed-websocket             | 10.1.31         |
| org.springframework          | spring-web                         | 6.1.14          |
| org.springframework          | spring-webmvc                      | 6.1.14          |
| org.springframework.boot     | spring-boot-starter-data-redis     | 3.3.5           |
| io.lettuce                    | lettuce-core                       | 6.3.2.RELEASE   |
| io.netty                      | netty-common                       | 4.1.114.Final   |
| io.netty                      | netty-handler                      | 4.1.114.Final   |
| io.projectreactor             | reactor-core                       | 3.6.11          |
| org.springframework.data      | spring-data-redis                  | 3.3.5           |
| org.springframework          | spring-oxm                         | 6.1.14          |
| org.springframework.boot     | spring-boot-starter-actuator       | 3.3.5           |
| io.micrometer                 | micrometer-observation             | 1.13.6          |
| io.micrometer                 | micrometer-jakarta9                | 1.13.6          |
| com.h2database                | h2                                 | 2.2.224         |
| com.mysql                     | mysql-connector-j                  | 8.3.0           |
| org.springframework.boot     | spring-boot-starter-test           | 3.3.5           |
| org.junit.jupiter             | junit-jupiter                      | 5.10.5          |
| org.mockito                   | mockito-core                       | 5.11.0          |
| org.springframework.security  | spring-security-test              | 6.3.4           |



### developmentassistant 서버 Dependency
| Group                          | Artifact                                | Version       |
|--------------------------------|-----------------------------------------|---------------|
| org.springframework.boot       | spring-boot                             | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-web                 | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-data-jpa            | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-data-redis          | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-validation          | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-cache               | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-actuator            | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-security            | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-oauth2-client       | 3.1.1         |
| org.springframework.cloud      | spring-cloud-starter-bootstrap          | 4.0.6         |
| org.springframework.cloud      | spring-cloud-starter-config             | 4.0.6         |
| org.springframework.cloud      | spring-cloud-starter-netflix-eureka-client | 4.0.6       |
| org.springframework.cloud      | spring-cloud-starter-loadbalancer       | 4.0.6         |
| org.springframework.security   | spring-security-config                  | 6.1.1         |
| org.springframework.security   | spring-security-core                    | 6.1.1         |
| org.springframework.security   | spring-security-web                     | 6.1.1         |
| org.springframework.security   | spring-security-oauth2-client           | 6.1.1         |
| org.springframework.security   | spring-security-oauth2-jose             | 6.1.1         |
| org.springframework.data       | spring-data-commons                     | 3.1.1         |
| io.jsonwebtoken                | jjwt-api                                | 0.11.5        |
| io.jsonwebtoken                | jjwt-impl                               | 0.11.5        |
| io.jsonwebtoken                | jjwt-jackson                            | 0.11.5        |
| com.fasterxml.jackson.core     | jackson-databind                        | 2.15.2        |
| com.fasterxml.jackson.datatype | jackson-datatype-jsr310                 | 2.15.2        |
| org.projectlombok              | lombok                                  | 1.18.28       |
| org.mapstruct                  | mapstruct                               | 1.5.5.Final   |
| org.aspectj                    | aspectjweaver                           | 1.9.20        |
| javax.xml.bind                 | jakarta.xml.bind-api                    | 4.0.0         |
| jakarta.persistence            | jakarta.persistence-api                 | 3.1.0         |
| org.apache.commons             | commons-lang3                           | 3.12.0        |
| org.springframework.retry      | spring-retry                            | 2.0.2         |
| io.github.resilience4j         | resilience4j-spring-boot3               | 2.0.2         |
| io.github.resilience4j         | resilience4j-retry                      | 2.0.2         |
| io.micrometer                  | micrometer-observation                  | 1.11.0        |
| org.springframework.boot       | spring-boot-configuration-processor     | 3.1.1         |
| org.springframework.kafka      | spring-kafka                            | 3.0.8         |
| org.springframework.kafka      | spring-kafka-test                       | 3.0.8         |


### SCG 서버 Dependency
| Group                          | Artifact                                | Version       |
|--------------------------------|-----------------------------------------|---------------|
| org.springframework.cloud      | spring-cloud-starter-gateway            | 4.0.6         |
| org.springframework.boot       | spring-boot                             | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-webflux             | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-validation          | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-data-redis          | 3.1.1         |
| io.netty                       | netty-codec-http                        | 4.1.93.Final  |
| io.projectreactor.netty        | reactor-netty-http                      | 1.1.6         |
| org.springframework.security   | spring-security-config                  | 6.1.1         |
| org.springframework.security   | spring-security-core                    | 6.1.1         |
| org.springframework.security   | spring-security-web                     | 6.1.1         |
| io.jsonwebtoken                | jjwt-api                                | 0.11.5        |
| io.jsonwebtoken                | jjwt-impl                               | 0.11.5        |
| io.jsonwebtoken                | jjwt-jackson                            | 0.11.5        |
| org.springframework.boot       | spring-boot-starter-logging             | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-reactor-netty       | 3.1.1         |
| com.fasterxml.jackson.core     | jackson-databind                        | 2.15.2        |
| com.fasterxml.jackson.datatype | jackson-datatype-jsr310                 | 2.15.2        |
| io.github.resilience4j         | resilience4j-spring-boot3               | 2.0.2         |
| io.github.resilience4j         | resilience4j-retry                      | 2.0.2         |
| io.micrometer                  | micrometer-observation                  | 1.11.0        |
| org.aspectj                    | aspectjweaver                           | 1.9.20        |
| org.springframework.retry      | spring-retry                            | 2.0.2         |
| org.springframework.cloud      | spring-cloud-starter-bootstrap          | 4.0.6         |
| org.springframework.cloud      | spring-cloud-starter-loadbalancer       | 4.0.6         |
| org.springframework.cloud      | spring-cloud-starter-circuitbreaker-resilience4j | 4.0.6 |
| org.springframework.cloud      | spring-cloud-starter-security           | 4.0.6         |
| org.springframework.boot       | spring-boot-configuration-processor     | 3.1.1         |
| org.springframework.boot       | spring-boot-starter-actuator            | 3.1.1         |



### face_recognition 서버 requirements
```
# Back-end (face_recognition)

# requirements.txt

fastapi
uvicorn
opencv-python-headless
deepface
pyjwt
tf-keras
qdrant-client
websockets
httpx
scipy
numpy

# qdrant

QDRANT_HOST = qdrant.eks-work2.svc.cluster.local
QDRANT_PORT = 6333


```

### rag 서버 requirements
```
# Back-end (rag)

# requirements.txt
flask #==2.3.3
transformers #==4.27.4 #4.33.2
chromadb
langchain
torch
openai
langchain
langchain-community
langchain-openai
langchain-chroma
langchain-core

tf-keras
sentence-transformers #==2.2.2
python-dotenv #==1.0.0
langchain-community

python-gitlab
gitpython

tree-sitter
tree-sitter-javascript
tree-sitter-python
tree-sitter-java
tree-sitter-c
tree-sitter-cpp

gunicorn

# ChromaDB

CHROMADB_URI = sqlite:///:memory:

# OpenAI API

OPENAI_API_KEY = <your-api-key>
```