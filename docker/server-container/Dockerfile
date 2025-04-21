# 1. 빌드 단계 (메이븐으로 의존성 해결 및 JAR 파일 생성)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY ../pom.xml ./
COPY ../src ./src
RUN mvn clean package -DskipTests

# 2. 실행 단계 (JAR 파일을 실행할 경량 이미지)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]