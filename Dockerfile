# 모든 서비스가 함께 쓰는 Dockerfile. 빌드 컨텍스트는 레포 루트여야 공통 모듈(common, contracts)을 찾는다.
#   docker build --build-arg SERVICE=ticket-core -t itoms/ticket-core .
# 보통은 infra/docker-compose.yml이 서비스마다 SERVICE를 넘겨 빌드한다.

FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY . .
ARG SERVICE
RUN test -n "$SERVICE" || (echo "SERVICE 빌드 인자가 필요합니다 (예: ticket-core)" && exit 1)
# Gradle 캐시를 빌드 사이에 재사용한다 (두 번째 서비스부터 의존성을 다시 받지 않음)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew ":${SERVICE}:bootJar" --no-daemon -q

FROM eclipse-temurin:21-jre
ENV TZ=Asia/Seoul
WORKDIR /app
ARG SERVICE
COPY --from=build /workspace/${SERVICE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]
