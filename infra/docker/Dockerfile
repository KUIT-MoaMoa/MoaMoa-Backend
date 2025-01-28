FROM openjdk:17-jdk

# 빌드 시 전달된 JAR_FILE 경로를 ARG로 받음
ARG JAR_FILE=../../build/libs/*.jar

# JAR_FILE을 컨테이너 내부의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]