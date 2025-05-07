# 1. 빌드 단계 (메이븐으로 의존성 해결 및 JAR 파일 생성)
# 이 이미지 기반으로 컨테이너를 만들겠다.
FROM maven:3.9.6-eclipse-temurin-21 AS builder
#컨테이너 내부에서 작업 디렉토리를 /app으로 설정.
#이후 모든 명령은 /app 디렉토리 안에서 실행됨.
WORKDIR /app
#로컬의 pom.xml 파일을 /app 내부로 복사(현재 작업 디렉토리)
COPY ../pom.xml ./
#로컬의 src 파일을 복사  ./ 로만 하면 디렉토리 내부 파일만 복사되어버림. 도커파일 기준으로
COPY ../src ./src
#maven을 사용하여 빌드
#-DskipTests : 테스트 실행을 건너뜀 (CI/CD에서는 속도 향상을 위해 종종 사용됨).
RUN mvn clean package -DskipTests


# 2. 실행 단계 (JAR 파일을 실행할 경량 이미지)
FROM eclipse-temurin:21-jre
WORKDIR /app
#builder 단계에서 생성된 JAR 파일을 실행 컨테이너로 복사한다.
#--from=builder : 첫 번째 빌드 단계(builder)에서 target/*.jar을 복사함.
COPY --from=builder /app/target/*.jar app.jar
#컨테이너가 외부에서 8080 포트로 접근할 수 있도록 설정.
#기본적으로 Docker는 포트를 자동으로 개방하지 않으므로, docker run -p 8080:8080 같은 명령을 실행해야 외부에서 접근 가능.
EXPOSE 8080
#컨테이너가 실행될 때 실행할 기본 명령을 지정.
#java -jar app.jar 명령을 실행하여 Spring Boot 애플리케이션을 시작.
ENTRYPOINT ["java","-jar","app.jar"]