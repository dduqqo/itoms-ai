# 빌드 기준선 (0단계)

멀티모듈 전환 **전**, 각 서비스 폴더에서 자체 `./gradlew build`를 실행한 결과다.
전환 중에 깨진 것과 원래 깨져 있던 것을 구분하기 위한 기록이다.

- 실행일: 2026-11-24
- 환경: macOS, OpenJDK 21.0.7, 로컬 MySQL·환경변수 없음

| 서비스 | 컴파일 | 테스트 | 실패 원인 |
|---|---|---|---|
| api-gateway | 통과 | 1/1 통과 | - |
| service-desk | 통과 | `contextLoads` 실패 | `${DB_URL}` 환경변수 없음 |
| ticket-core | 통과 | 4/4 실패 | 아래 참고 |
| user-service | 통과 | `contextLoads` 실패 | `${DB_URL}` 환경변수 없음 |
| service-request-management | 통과 | `contextLoads` 실패 | DB 연결 불가 (Hibernate Dialect 결정 실패) |

ticket-core 상세:
- `contextLoads`, `RestGlobalExceptionHandlerTest` 2건: `${INCIDENT_URL}` 환경변수가 없어 Feign URL 생성 실패 (스프링 컨텍스트를 띄우는 테스트)
- `TicketTest.초기화_티켓_생성`: **테스트 자체 버그.** `UUID.randomUUID()`로 만든 incidentId를 문자열 `"test_incident_id"`와 비교한다.

## 1단계(빌드 묶기) 후 결과

루트에서 `./gradlew assemble`은 5개 서비스 모두 통과했다.
`./gradlew test` 결과는 위 표와 **실패 테스트와 원인이 모두 같다.** 전환으로 새로 깨진 테스트는 없다.

`./gradlew build`가 통과하려면 테스트용 DB 설정(H2 또는 Testcontainers)과 `TicketTest` 수정이 필요하다. 코드 변경이므로 1단계에서는 하지 않았다.
