FROM openjdk:17-jdk

# 빌드 시 전달된 JAR_FILE 경로를 ARG로 받음
ARG JAR_FILE=../../build/libs/*.jar
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_PASSWORD
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG NAVER_CLIENT_ID
ARG NAVER_CLIENT_SECRET
ARG JWT_SECRET
ARG JWT_EXPIRATION

# JAR_FILE을 컨테이너 내부의 app.jar로 복사
COPY ${JAR_FILE} app.jar

ENV MYSQL_URL=${MYSQL_URL}
ENV MYSQL_USER=${MYSQL_USER}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV NAVER_CLIENT_ID=${NAVER_CLIENT_ID}
ENV NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET}
ENV JWT_SECRET=${JWT_SECRET}
ENV JWT_EXPIRATION=${JWT_EXPIRATION}

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]