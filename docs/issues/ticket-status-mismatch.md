# [이슈] ticket-core와 service-request-management의 TicketStatus 불일치

4단계(contracts) 작업 중 발견. 계획서 4단계: "다르다면 서비스 간 상태 불일치 버그의 원인일 수 있으므로 별도 이슈로 정리한다."

## 현상

| 이름 | code | codeName | ticket-core | service-request-management |
|---|---|---|---|---|
| `REQUEST_CREATE_TICKET` | 1 | REQUEST | O | **X** |
| `COMPLETE_ASSIGN_CREATOR` | 2 | CREATED | O | **X** |
| `PENDING_EXECUTION` | 3 | PENDING_EXECUTION | O | O |
| `DEACTIVATE_EXECUTION` | 4 | NOT_STARTED_EXECUTION | O | O |
| `ACTIVATE_EXECUTION` | 5 | IN_PROGRESS | O | O |
| `COMPLETE_EXECUTION` | 6 | COMPLETE_EXECUTION | O | O |
| `CANCEL_EXECUTION` | 7 | REJECTED | O | O |
| `PAUSED_EXECUTION` | 9 | HOLD_ON | O | O |

3~9는 이름·code·codeName·설명이 모두 같다. 두 서비스 모두 DB에 `@Enumerated(EnumType.STRING)`(이름)으로 저장한다.

## 영향

- service-request-management가 `POST /api/ticket-core/v1/tickets/by-acceptor`로 받은 응답(`TicketInformationDTO.ticketStatus`)을 자기 enum으로 역직렬화한다.
  ticket-core가 상태 1, 2인 티켓을 돌려주면 **역직렬화에 실패해 목록 조회 전체가 실패**한다.
- 담당자가 정해진 티켓만 조회하므로 실제로는 3 이상일 가능성이 높지만, 이를 보장하는 코드는 없다.

## 이 때문에 보류한 것

담당자별 티켓 목록 응답 DTO(ticket-core `TicketInfoResponseDTO`, SRM `TicketInformationDTO`)는 필드가 같지만 `TicketStatus` 필드 때문에 contracts로 합치지 않았다.

## 결정할 것

1. **SRM이 상태 1, 2를 몰라도 되는가?** (SRM은 담당자 할당 이후의 티켓만 다룬다는 설계 의도인지)
2. 합친다면 방식:
   - A. 8개 값을 모두 가진 `TicketStatus`를 contracts에 두고 두 서비스가 함께 쓴다. DB는 이름으로 저장하므로 기존 데이터에 영향이 없다.
     단, ticket-core의 `fromCodeName`(업무 예외를 던짐)은 contracts에 둘 수 없으므로 ticket-core에 남긴다.
   - B. contracts DTO의 `ticketStatus`를 `String`(enum 이름)으로 두고, 각 서비스가 자기 enum으로 바꾼다. 받는 쪽이 모르는 값을 직접 처리할 수 있다.
