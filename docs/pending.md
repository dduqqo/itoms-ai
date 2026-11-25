# 보류·추가 확인 사항

멀티모듈 전환(0~4단계) 중에 결정을 미루거나, 다음 단계로 넘기거나, 직접 확인하지 못한 항목을 모은 문서입니다.
처리하면 이 문서에서 지우거나 `처리됨`으로 표시합니다.

- 기준일: 2026-11-25 (5단계 반영)
- 계획서: `ITOMS_MULTIMODULE_PLAN.md` (레포 밖, 상위 폴더)

---

## 1. 결정 보류

팀 확인이 필요해서 일부러 미뤄 둔 항목입니다.

| 항목 | 현재 상태 | 결정할 것 | 관련 문서 |
|---|---|---|---|
| **TicketStatus 불일치** | ticket-core는 8개 값, service-request-management는 6개 값(상태 1, 2 없음). 각자 그대로 둠 | 예전에 정한 상태값 설계 의도 확인 후, 공유 방식 결정 | [issues/ticket-status-mismatch.md](issues/ticket-status-mismatch.md) |
| 담당자별 티켓 목록 DTO | ticket-core `TicketInfoResponseDTO`, SRM `TicketInformationDTO`가 따로 있음 (필드는 같음) | 위 TicketStatus 결정 후 contracts로 합칠지 | [../contracts/README.md](../contracts/README.md) |
| **결과 코드 접두어** | 임시 값 `INCIDENT`, `TICKET`, `USER`, `SR` 사용 중. `COMMON`만 확정 | 서비스 이름 기준인지 도메인 기준인지, 약어 허용 여부, 기존 `MEMBER` 유지 여부 | [../common/common-response/README.md](../common/common-response/README.md) 3장 |

접두어를 확정하면 바꿀 곳: `TicketErrorCode`(ticket-core), `SrErrorCode`(service-request-management), README 접두어 표.

---

## 2. 다음 단계로 넘긴 작업

| 항목 | 넘긴 단계 | 내용 |
|---|---|---|
| ticket-core 임시 CORS | 7~8단계 | 게이트웨이가 ticket-core를 라우팅하기 전까지 브라우저에서 직접 테스트하려고 둔 설정. 게이트웨이로 CORS를 옮긴 뒤 `cors.allowed.*`와 쓰지 않게 된 `CorsAutoConfiguration`을 지운다 |
| ~~로컬 기본 포트 충돌~~ | **처리됨** (5단계) | FK-70 병합 후 service-desk, ticket-core, user-service, service-request-management가 모두 기본 8080. 동시에 띄우면 충돌한다 |
| ~~Dockerfile~~ | **처리됨** (5단계, 루트 공용 Dockerfile) | FK-70 버전으로 바뀌었고, 서비스 폴더 안에서 빌드하는 방식이라 멀티모듈에서는 빌드되지 않는다. user-service, service-request-management에도 FK-70에서 Dockerfile이 생겼다 |
| 서비스 폴더 안의 `.github/workflows` | 6단계 | 레포 루트에 있지 않아 GitHub에서 실행되지 않는다 |
| 기존 운영 배포 방식 | 6단계 | 예전 infra compose는 Docker Hub 이미지(`fkiller14800/itoms_*`)를 받아 실행했다. 새 compose는 소스에서 빌드한다. 운영 서버에 이미지를 올리는 방식은 CI에서 다시 정한다 |
| 게이트웨이 라우팅 범위 | 7~8단계 | 게이트웨이는 service-desk `/api/incident`, user-service `/api/user` 두 경로만 연결한다. ticket-core, service-request-management는 아직 게이트웨이로 호출할 수 없다. 경로를 `/**` 없이 적어서 정확히 일치할 때만 전달한다(`/api/incident/123`, `/api/incident/v1/...`는 통과하지 못함) |
| 게이트웨이 자체 에러 형식 | 7~8단계 | 뒤쪽 서비스가 꺼져 있는 등 게이트웨이가 직접 돌려주는 에러는 Spring 기본 형식이고 `ApiResponse`가 아니다. 클라이언트가 두 형식을 모두 처리해야 할 수 있다. 게이트웨이 에러도 `ApiResponse`로 맞출지 결정 |
| 관측 스택 수집 대상 | 7~8단계 | Prometheus는 service-desk, user-service만 수집한다. ticket-core, service-request-management, api-gateway는 대상과 actuator 노출 설정이 없다 |
| 서비스별 DB 분리 | 미정 | 모든 서비스가 MySQL DB 하나를 함께 쓴다 (기존 운영 compose와 같음). 예전 `test.yaml` 주석에 서비스별 DB로 나누자는 계획이 있었다 |
| ~~0단계 남은 정리~~ | **처리됨** (5단계) | `.DS_Store`·서비스별 ignore 파일 삭제. 평문 비밀번호는 이미 남아 있지 않았고, compose 비밀값은 `infra/.env`로 분리 |
| 깨진 테스트 | 미정 | DB를 쓰는 서비스 4곳의 `contextLoads`는 DB·환경변수가 없으면 실패한다(테스트용 DB 설정 필요: H2 또는 Testcontainers). ticket-core `TicketTest`는 테스트 코드 버그(랜덤 UUID를 고정 문자열과 비교). 전환 전부터 깨져 있었다 ([build-baseline.md](build-baseline.md)) |

---

## 3. 직접 확인하지 못한 것

DB, Kafka, 실행 환경변수가 없어서 **컴파일과 단위 테스트까지만** 확인했습니다. 서비스를 띄우는 환경이 준비되면 아래를 확인합니다.

### 실행 환경변수

| 확인할 것 | 이유 |
|---|---|
| `INCIDENT_URL`에 `/api/incident`가 포함돼 있는지 (compose는 포함해서 설정함, **IDE 실행 설정만 확인 필요**) | ticket-core `IncidentClient`가 `/v1/ticket/complete`만 붙여 호출한다. service-desk의 실제 경로는 `/api/incident/v1/ticket/complete` |
| `TICKET_CORE_URL`에 `/api/ticket-core`가 포함돼 있는지 (compose는 포함해서 설정함, **IDE 실행 설정만 확인 필요**) | service-request-management `TicketCoreClient`가 `/v1/tickets/by-acceptor`, `/callback/...`만 붙여 호출한다 |
| 더 이상 쓰지 않는 값 정리 | `LOGGER_LEVEL`(Feign 로그), `SERVICE_REQUEST_MANAGEMENT_URL`(빈 Feign 클라이언트), SRM의 `ALLOWED_*`(CORS 제거) |

### 동작

| 확인할 것 | 관련 변경 |
|---|---|
| ticket-core, service-request-management의 `modified_at` 컬럼 매핑 | `BaseEntity` 통합 시 `@AttributeOverride`로 기존 컬럼 이름 유지 |
| Swagger 화면의 문서 제목 | `SwaggerConfig` 4개를 `itoms.swagger.*` 설정으로 대체 |
| `@Async` 스레드 풀 | `AsyncConfig`를 `spring.task.execution.*`로 대체 (값 동일) |
| ~~서비스 간 HTTP 호출~~ | **확인됨** (5단계 compose): 게이트웨이 → service-desk → user-service, ticket-core → user-service 호출 성공 |
| 트레이스가 서비스 사이에 이어지는지 | `HttpServiceClients`가 Spring Boot의 `RestClient.Builder` 사용 |
| ticket-core, service-request-management의 Zipkin 전송 | 관측 의존성이 새로 적용됨. 기본 주소 `localhost:9411` |

### 다른 사람에게 영향이 가는 변경

| 확인할 것 | 내용 |
|---|---|
| **프론트엔드 응답 처리** | ticket-core, service-request-management의 성공 응답이 `ApiResponse`로 감싸졌다. 값은 `result`에서 꺼내야 한다. 204를 보내던 콜백도 200으로 바뀌었다 |
| 에러 응답 형식 | ticket-core, service-request-management 에러가 `{message, detail}`에서 `{isSuccess, code, message}`로 바뀌었다 |

---

## 4. FK-70(REST vs Kafka 실험) 병합으로 생긴 것

| 항목 | 내용 | 확인할 것 |
|---|---|---|
| `logback-spring.xml` 삭제 | service-desk, user-service에서 삭제됨 (원래 커밋: "docker compose 에러 때문에 삭제") | Loki로 로그가 가지 않는다. 관측 스택 활성화(7~8단계) 때 다시 넣을지 |
| 티켓 완료 알림 이중 전송 | ticket-core가 완료 시 REST(→ service-desk)와 Kafka(→ service-request-management)로 둘 다 보냄 | 실험용이라면 운영 방식 하나로 정리할지 |
| Kafka 엔드포인트 | service-desk에 `/api/incident-kafka` 추가 | 실험이 끝나면 유지할지 |
| **Kafka 티켓 생성 실패 (원본 버그)** | service-desk가 Kafka로 보내는 티켓 생성 요청의 `requester`가 `null`인데, ticket-core는 이를 필수로 사용해 `NullPointerException`. 10번 재시도 후 메시지를 버리고, 장애의 티켓 ID가 저장되지 않는다. 원본 FK-70 코드에서도 같다 (5단계 compose에서 재현) | Kafka 경로에서 요청자 정보를 언제 넣을지 (user-service 응답 후 발행 등) |

---

## 5. 모노레포에 들어오지 않은 원본 브랜치

원본 레포(F-kill3r)에서 지금 모노레포에 포함되지 않은 커밋이 있는 브랜치입니다.
병합 기준: 각 레포의 `dev`(api-gateway, infra는 `main`) + `Feature/FK-70`.

| 서비스 | 브랜치 | 커밋 | 최근 | 마지막 커밋 |
|---|---|---|---|---|
| service-desk | `feature/FK-44` | 1 | 2025-05-28 | feat: 통신방식 동적 설정 |
| service-desk | `feature/FK-73` | 2 | 2025-06-18 | feat: gRPC 프로토콜 추가 |
| ticket-core | `ANseunghyeon-patch-1` | 3 | 2025-06-06 | Update Dockerfile |
| ticket-core | `feature/FK-68` | 1 | 2025-06-07 | feat: 작업자 자동 할당 로직 추가 |
| ticket-core | `feature/FK-69` | 1 | 2025-06-18 | feat: gRPC 프로토콜 구현 |
| user-service | `Feature/FK-73` | 1 | 2025-06-18 | feat: gRPC 프로토콜 통신 |
| service-request-management | `chore/FK-62` | 1 | 2025-06-06 | chore: add Dockerfile |

특히 확인할 것:
- **`feature/FK-68` 작업자 자동 할당**: 업무 기능이다. 7~8단계 기능 작업과 겹치는지
- **FK-69, FK-73 gRPC**: FK-70(Kafka)과 같은 통신 방식 비교 실험으로 보인다. 포함할지
