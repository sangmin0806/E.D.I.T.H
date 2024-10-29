# 빌드 단계
FROM gradle:7.6.1-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 소스 코드 복사
COPY . .

# 빌드 및 문서 생성
RUN gradle clean build

# 실행 단계
FROM openjdk:17

# 빌드 시 JWT_SECRET 환경 변수 설정
ARG SPRING_JWT_SECRET
ENV SPRING_JWT_SECRET=$SPRING_JWT_SECRET

# 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone

# JAR 파일 복사
COPY --from=builder /home/gradle/project/build/libs/SCG-0.0.1-SNAPSHOT.jar /SCG.jar

# 실행
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "-Dserver.port=8080", "-Duser.timezone=Asia/Seoul", "/SCG.jar"]
