FROM amazoncorretto:17
LABEL maintainer="minju"

RUN yum install -y glibc-langpack-ko
ENV LANG ko_KR.UTF8
ENV LC_ALL ko_KR.UTF8
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} scg.jar

ENTRYPOINT ["java", \
 "-verbose:gc", \
 "-Xlog:gc*:stdout:time,uptime,level,tags", \
 "-Djava.security.egd=file:/dev/./urandom", \
 "-Dspring.profiles.active=prod", \
 "-jar", \
 "/scg.jar", \
 "--server.port=8080"]



## 빌드 단계
#FROM gradle:7.6.1-jdk17 AS builder
#
## 작업 디렉토리 설정
#WORKDIR /home/gradle/project
#
## 소스 코드 복사
#COPY . .
#
## 빌드 및 문서 생성
#RUN gradle clean build -x test
#
## 실행 단계
#FROM openjdk:17
#
## 타임존 설정
#RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone
#
## JAR 파일 복사
#COPY --from=builder /home/gradle/project/build/libs/SCG-0.0.1-SNAPSHOT.jar /SCG.jar
#
## 실행
#ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "-Dserver.port=8080", "-Duser.timezone=Asia/Seoul", "/SCG.jar"]
