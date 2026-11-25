# 로컬 실행 환경변수

서비스를 IDE(IntelliJ 등)에서 직접 실행할 때 넣어야 하는 값입니다.
`docker compose`로 전부 띄울 때는 compose가 알아서 넣으므로 이 문서가 필요 없습니다 (`infra/.env`만 채우면 됨).

## 0. 준비: MySQL(필요하면 Kafka)만 컨테이너로 띄우기

```
cp infra/.env.example infra/.env     # 처음 한 번, DB_PASSWORD 채우기
docker compose -f infra/docker-compose.yml up -d mysql          # MySQL만
docker compose -f infra/docker-compose.yml up -d mysql kafka    # Kafka 경로까지 쓸 때
```

- MySQL 호스트 포트: 기본 `3306`, `infra/.env`의 `MYSQL_HOST_PORT`로 변경
- Kafka 호스트 포트: 기본 `9092`, `infra/.env`의 `KAFKA_HOST_PORT`로 변경
- DB 이름·계정·비밀번호는 `infra/.env`의 `DB_NAME`, `DB_USER`, `DB_PASSWORD`와 같게 넣는다

## 1. 공통 (DB를 쓰는 서비스 4곳 모두)

| 변수 | 값 (기본 포트 기준) | 설명 |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/itoms?serverTimezone=Asia/Seoul&characterEncoding=UTF-8` | 포트는 `MYSQL_HOST_PORT`, DB 이름은 `DB_NAME` |
| `DB_USER` | `root` | `infra/.env`의 `DB_USER` |
| `DB_PASSWORD` | `infra/.env`의 값 | |
| `DDL_ENV` | `update` | `create` / `update` / `validate` / `none` |
| `SWAGGER_PATH` | (빈 값) | Swagger 경로 앞에 붙는 값. 비우면 `/swagger-ui.html` |

### 선택 (넣지 않으면 기본값)

| 변수 | 기본값 | 넣는 경우 |
|---|---|---|
| `SERVER_PORT` | 서비스별 (아래 표) | 포트가 이미 쓰이고 있을 때 |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | compose Kafka를 다른 포트로 띄웠을 때. **9092에 다른 Kafka가 떠 있으면 그쪽에 붙으니 주의** |
| `MANAGEMENT_TRACING_ENABLED` | `true` | 관측 스택(Zipkin/Tempo) 없이 띄울 때 `false`. 안 끄면 트레이스 전송 실패 로그가 남는다 |

## 2. 서비스별

| 서비스 | 기본 포트 | 필수 변수 (공통 제외) |
|---|---|---|
| api-gateway | 9000 | 없음 (DB 안 씀). 선택: `SERVICE_DESK_URL`(기본 `http://localhost:8080`), `USER_SERVICE_URL`(기본 `http://localhost:8081`), `LOKI_URL` |
| service-desk | 8080 | `USER_SERVICE_URL`=`http://localhost:8081`, `TICKET_SERVICE_URL`=`http://localhost:8082` |
| user-service | 8081 | 없음 |
| ticket-core | 8082 | `INCIDENT_URL`=`http://localhost:8080/api/incident` (**`/api/incident`까지 넣는다**), `USER_SERVICE_URL`=`http://localhost:8081`, `ALLOWED_PATH`=`/**`, `ALLOWED_PATTERN`=`http://localhost:*`, `ALLOWED_METHOD`=`GET,POST,PUT,PATCH,DELETE,OPTIONS` |
| service-request-management | 8083 | `TICKET_CORE_URL`=`http://localhost:8082/api/ticket-core` (**`/api/ticket-core`까지 넣는다**) |

- service-desk, user-service는 `local` 프로필로 뜬다 (`application.yml`의 `spring.profiles.active: local`). DB 설정은 `application-local.yml`에 있다.
- 다른 서비스의 포트를 바꿨다면 그 주소를 쓰는 변수(`*_URL`)도 같이 바꾼다.
- **ticket-core는 DB가 비어 있지 않으면 두 번째 실행부터 시작에 실패한다.** 시작할 때마다 `data.sql`을 그냥 `INSERT`해서 기본키가 겹친다([pending.md](pending.md) 참고). 고치기 전까지는 `SPRING_SQL_INIT_CONTINUE_ON_ERROR=true`를 같이 넣는다 (시드 데이터가 이미 있으면 넘어감).
- `ALLOWED_*`는 ticket-core 임시 CORS용이다. 게이트웨이로 CORS를 옮기면(7~8단계) 없어진다.

## 3. IntelliJ에 붙여 넣을 값

Run Configuration → Environment variables 칸에 그대로 붙여 넣는다 (`;`로 구분). `DB_PASSWORD`만 채운다.

**service-desk**
```
DB_URL=jdbc:mysql://localhost:3306/itoms?serverTimezone=Asia/Seoul&characterEncoding=UTF-8;DB_USER=root;DB_PASSWORD=;DDL_ENV=update;SWAGGER_PATH=;MANAGEMENT_TRACING_ENABLED=false;USER_SERVICE_URL=http://localhost:8081;TICKET_SERVICE_URL=http://localhost:8082
```

**user-service**
```
DB_URL=jdbc:mysql://localhost:3306/itoms?serverTimezone=Asia/Seoul&characterEncoding=UTF-8;DB_USER=root;DB_PASSWORD=;DDL_ENV=update;SWAGGER_PATH=;MANAGEMENT_TRACING_ENABLED=false
```

**ticket-core**
```
DB_URL=jdbc:mysql://localhost:3306/itoms?serverTimezone=Asia/Seoul&characterEncoding=UTF-8;DB_USER=root;DB_PASSWORD=;DDL_ENV=update;SWAGGER_PATH=;MANAGEMENT_TRACING_ENABLED=false;INCIDENT_URL=http://localhost:8080/api/incident;USER_SERVICE_URL=http://localhost:8081;ALLOWED_PATH=/**;ALLOWED_PATTERN=http://localhost:*;ALLOWED_METHOD=GET,POST,PUT,PATCH,DELETE,OPTIONS;SPRING_SQL_INIT_CONTINUE_ON_ERROR=true
```

**service-request-management**
```
DB_URL=jdbc:mysql://localhost:3306/itoms?serverTimezone=Asia/Seoul&characterEncoding=UTF-8;DB_USER=root;DB_PASSWORD=;DDL_ENV=update;SWAGGER_PATH=;MANAGEMENT_TRACING_ENABLED=false;TICKET_CORE_URL=http://localhost:8082/api/ticket-core
```

**api-gateway**
```
MANAGEMENT_TRACING_ENABLED=false
```

## 4. 더 이상 쓰지 않는 값

예전 실행 설정에 남아 있다면 지워도 된다.

| 변수 | 이유 |
|---|---|
| `LOGGER_LEVEL` | Feign 로그 레벨. HTTP Interface로 바꾸면서 안 씀 |
| `SERVICE_REQUEST_MANAGEMENT_URL` | 빈 Feign 클라이언트용. 삭제됨 |
| service-request-management의 `ALLOWED_*` | SRM CORS 설정 삭제 |
