FROM amazoncorretto:17
LABEL maintainer="minju"

RUN yum install -y glibc-langpack-ko
ENV LANG ko_KR.UTF8
ENV LC_ALL ko_KR.UTF8
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} developmentassistant.jar

ENTRYPOINT ["java", \
 "-verbose:gc", \
 "-Xlog:gc*:stdout:time,uptime,level,tags", \
 "-Djava.security.egd=file:/dev/./urandom", \
 "-jar", \
 "/developmentassistant.jar", \
 "--server.port=8082"]



## 빌드 스테이지
#FROM gradle:7.6.0-jdk17-alpine AS build
#
#WORKDIR /home/gradle/project
#
## 소스 코드를 빌드 스테이지에 복사
#COPY . .
#
## 애플리케이션 빌드
#RUN gradle clean build -x test
#
## 실행 스테이지
#FROM openjdk:17-jdk-alpine
#
#WORKDIR /usr/app
#
## 빌드 스테이지에서 JAR 파일을 실행 스테이지로 복사
#COPY --from=build /home/gradle/project/build/libs/developmentassistant-0.0.1-SNAPSHOT.jar /usr/app/developmentassistant-0.0.1-SNAPSHOT.jar
#
## 애플리케이션이 사용하는 포트를 노출
#EXPOSE 8082
#
## 애플리케이션 실행
#ENTRYPOINT ["java", "-jar", "/usr/app/developmentassistant-0.0.1-SNAPSHOT.jar"]
