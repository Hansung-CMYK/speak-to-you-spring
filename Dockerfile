# 1️⃣ 빌드 스테이지 (Gradle 빌드 실행)
FROM amazoncorretto:23 AS builder

WORKDIR /app

# Gradle 관련 파일 복사 (캐싱 최적화)
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/ src/

# 필수 유틸리티 설치 (xargs 포함)
RUN yum install -y findutils

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# Gradle 빌드 실행 (JAR 파일 생성)
RUN ./gradlew bootJar

# 2️⃣ 실행 스테이지 (Spring Boot 실행)
FROM amazoncorretto:23

WORKDIR /app

# 빌드된 JAR 파일만 실행 컨테이너에 포함
COPY --from=builder /app/build/libs/*.jar app.jar

# 8080 포트 노출
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

# 타임존 설정
ENV TZ=Asia/Seoul
