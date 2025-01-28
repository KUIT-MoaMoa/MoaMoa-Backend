FROM openjdk:17-jdk

# 빌드 시 전달된 JAR_FILE 경로를 ARG로 받음
ARG JAR_FILE=../../build/libs/*.jar
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_PASSWORD

# JAR_FILE을 컨테이너 내부의 app.jar로 복사
COPY ${JAR_FILE} app.jar

ENV MYSQL_URL=${MYSQL_URL}
ENV MYSQL_USER=${MYSQL_USER}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]