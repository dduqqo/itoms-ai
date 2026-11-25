# contracts

서비스 사이를 오가는 **요청·응답 DTO와 메시지**를 모은 모듈입니다. REST(Feign, HTTP Interface)와 Kafka 메시지 모두 해당합니다.

보내는 쪽과 받는 쪽이 같은 클래스를 쓰므로, 필드를 바꾸면 관련 서비스가 모두 컴파일 에러를 냅니다.
실행해 보고 나서야 형식 불일치를 발견하는 일을 막기 위한 모듈입니다.

## 넣는 것

- 다른 서비스로 보내거나 다른 서비스에서 받는 DTO
- 클래스 주석에 **누가 누구에게 보내는지**(경로나 토픽)를 적습니다.

## 넣지 않는 것

- 엔티티, 리포지토리, 업무 로직
- 한 서비스 안에서만 쓰는 DTO (예: 프론트엔드 전용 요청)
- 서비스의 업무 enum (예: `UserCategory`). 값이 필요하면 DTO에 `String`(enum 이름)으로 둡니다.
  enum을 공유하면 그 enum을 바꿀 때 모든 서비스의 DB 매핑과 업무 로직이 함께 묶이기 때문입니다.

## 패키지

| 패키지 | 내용 |
|---|---|
| `contracts.ticket` | 티켓·작업 관련 (service-desk, ticket-core, service-request-management) |
| `contracts.user` | 사용자 관련 (user-service, service-desk, ticket-core) |

## 바꿀 때

배포된 서비스끼리는 컴파일 검사가 없습니다. 한쪽만 먼저 배포되는 동안에도 깨지지 않게 바꿉니다.

- 필드 추가: 받는 쪽이 없어도 동작하도록 선택값으로 추가합니다.
- 필드 삭제·이름 변경: 받는 쪽이 옛 필드와 새 필드를 모두 받도록 먼저 배포하고, 보내는 쪽을 나중에 배포합니다.

## 보류 중

- 담당자별 티켓 목록 응답: `TicketStatus` 불일치 때문에 보류 ([docs/issues/ticket-status-mismatch.md](../docs/issues/ticket-status-mismatch.md))
