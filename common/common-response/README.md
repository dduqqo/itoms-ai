# common-response

ITOMS 서비스가 따르는 **표준 HTTP 응답 형식**입니다.
모든 서비스는 성공과 실패 모두 아래 형식으로 응답합니다.

> 이 문서가 응답 형식의 원본입니다. Java 클래스는 이 명세를 구현한 것입니다.
> Java가 아닌 클라이언트(프론트엔드, 다른 언어로 만든 서비스, MCP 도구)는 이 문서를 기준으로 응답을 해석합니다.

---

## 1. 응답 형식

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `isSuccess` | boolean | O | 요청 성공 여부 |
| `code` | string | O | 결과 코드. [3. 결과 코드](#3-결과-코드) 참고 |
| `message` | string | O | 사람이 읽는 설명. 클라이언트는 이 값으로 분기하지 않습니다 |
| `result` | any | X | 성공 시 응답 데이터, 실패 시 추가 정보. 값이 없으면 **필드 자체를 생략**합니다 |

### 성공

```http
HTTP/1.1 200 OK
```
```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {
    "ticketId": "c7c1a115-bd4c-44b4-ad6b-210be37fb7d4"
  }
}
```

응답 데이터가 없는 경우(예: 콜백 처리)에는 `result`를 생략합니다.

```json
{ "isSuccess": true, "code": "COMMON200", "message": "성공입니다." }
```

### 실패: 업무 에러

```http
HTTP/1.1 404 Not Found
```
```json
{
  "isSuccess": false,
  "code": "TICKET4041",
  "message": "티켓을 찾을 수 없습니다."
}
```

### 실패: 입력값 검증

`result`에 **필드 이름과 에러 메시지**를 담습니다. 한 필드에 에러가 여러 개면 `, `로 이어 붙입니다.

```http
HTTP/1.1 400 Bad Request
```
```json
{
  "isSuccess": false,
  "code": "COMMON400",
  "message": "잘못된 요청입니다.",
  "result": {
    "title": "제목은 필수입니다.",
    "priority": "1 이상이어야 합니다., 5 이하여야 합니다."
  }
}
```

### 실패: 서버 내부 에러

```http
HTTP/1.1 500 Internal Server Error
```
```json
{
  "isSuccess": false,
  "code": "COMMON500",
  "message": "서버 에러, 관리자에게 문의 바랍니다."
}
```

---

## 2. 규칙

1. **HTTP 상태 코드는 실제 결과를 따릅니다.** 실패한 요청에 `200 OK`를 보내지 않습니다.
   클라이언트는 HTTP 상태로 성공·실패 범주를 판단하고, `code`로 세부 원인을 판단합니다.
2. **`code`로 분기하고 `message`로는 분기하지 않습니다.** `message`는 언제든 문구가 바뀔 수 있습니다.
3. **500 응답에는 내부 정보를 담지 않습니다.** 예외 메시지, 스택 트레이스, SQL은 응답에 넣지 않고 로그에만 남깁니다.
4. **성공 응답은 컨트롤러에서 직접 감쌉니다.** `ResponseBodyAdvice`로 자동으로 감싸지 않습니다.
   자동으로 감싸면 Swagger, actuator 응답까지 바뀌기 때문입니다.
5. **이 형식이 적용되지 않는 경로:** `/actuator/**`, `/v3/api-docs/**`, `/swagger-ui/**`

---

## 3. 결과 코드

### 형식

```
{접두어}{HTTP 상태 3자리}{순번}
```

- 예: `TICKET4041` → ticket-core의 404 에러 중 1번
- 순번은 1부터 시작하고, 같은 접두어와 HTTP 상태 안에서 겹치지 않게 붙입니다.
- **한 번 공개한 코드는 의미를 바꾸지 않습니다.** 더 쓰지 않게 된 코드는 지우고, 그 번호를 다른 의미로 다시 쓰지 않습니다.

### 접두어

> **추가 고려사항:** 서비스별 접두어 이름은 아직 확정하지 않았습니다. 아래 표는 임시 값이며, `COMMON`만 확정입니다.
> 확정할 때 고려할 점: 서비스 이름과 도메인 이름 중 무엇을 기준으로 할지, `SR`처럼 약어를 허용할지, 기존 `MEMBER` 접두어를 유지할지

| 접두어 | 정의하는 곳 |
|---|---|
| `COMMON` | common-response (모든 서비스 공통) |
| `INCIDENT` | service-desk |
| `TICKET` | ticket-core |
| `USER` | user-service |
| `SR` | service-request-management |

새 서비스를 추가하면 접두어를 이 표에 먼저 등록합니다.

### 공통 코드 (`COMMON`)

| code | HTTP 상태 | message | 쓰는 경우 |
|---|---|---|---|
| `COMMON200` | 200 | 성공입니다. | 모든 성공 응답 |
| `COMMON400` | 400 | 잘못된 요청입니다. | 입력값 검증 실패, 요청 형식 오류 |
| `COMMON401` | 401 | 인증이 필요합니다. | 인증 정보 없음 또는 만료 |
| `COMMON403` | 403 | 금지된 요청입니다. | 권한 없음 |
| `COMMON404` | 404 | 요청한 리소스를 찾을 수 없습니다. | 존재하지 않는 경로 |
| `COMMON500` | 500 | 서버 에러, 관리자에게 문의 바랍니다. | 처리하지 못한 예외 |

업무 에러(예: "티켓을 찾을 수 없습니다")는 `COMMON404`가 아니라 **각 서비스의 코드**(`TICKET4041`)를 씁니다.

위 표에 없는 Spring MVC 기본 오류(405 메서드 불일치, 415 미디어 타입 불일치 등)는 `COMMON{HTTP 상태}` 코드와 HTTP 표준 사유를 message로 씁니다.
예: `{"isSuccess": false, "code": "COMMON405", "message": "Method Not Allowed"}`

---

## 4. 사용법 (Java)

### 의존성 추가

```groovy
dependencies {
    implementation project(':common:common-response')
}
```

자동 설정으로 등록되므로 예외 핸들러를 위한 별도 설정이나 `@ComponentScan`이 필요 없습니다.

### 성공 응답

```java
@PostMapping
public ResponseEntity<ApiResponse<CreateTicketResponseDTO>> createTicket(@RequestBody @Valid CreateTicketRequestDTO request) {
    return ResponseEntity.ok(ApiResponse.onSuccess(ticketService.create(request)));
}
```

### 서비스 에러 코드 정의

에러 코드는 **각 서비스 안에** enum으로 정의하고 `BaseErrorCode`를 구현합니다.
업무 에러를 common-response에 추가하지 않습니다.

```java
@Getter
@AllArgsConstructor
public enum TicketErrorCode implements BaseErrorCode {
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "TICKET4041", "티켓을 찾을 수 없습니다."),
    TICKET_ALREADY_COMPLETED(HttpStatus.CONFLICT, "TICKET4091", "이미 완료된 티켓입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
```

### 예외 던지기

```java
throw new GeneralException(TicketErrorCode.TICKET_NOT_FOUND);

// 추가 정보를 result에 담을 때
throw new GeneralException(TicketErrorCode.TICKET_ALREADY_COMPLETED, Map.of("ticketId", ticketId));
```

### 다른 서비스 응답 받기

다른 서비스를 호출하는 클라이언트도 같은 `ApiResponse`로 받습니다. 응답 클래스를 서비스마다 복사해 두지 않습니다.

```java
@GetExchange("/api/user/randomRequester")
ApiResponse<UserCreateResponseDTO> getRandomRequesterUser();
```

---

## 5. 구성

| 클래스 | 역할 |
|---|---|
| `ApiResponse<T>` | 응답 형식. `onSuccess(result)`, `onFailure(code, message, result)` |
| `BaseErrorCode` | 에러 코드 인터페이스. `getHttpStatus()`, `getCode()`, `getMessage()` |
| `CommonErrorCode` | `COMMON` 접두어 코드 |
| `GeneralException` | 업무 예외. `BaseErrorCode`와 선택적 `detail`을 가짐 |
| `GlobalExceptionHandler` | 예외를 위 형식으로 바꿔 응답. 자동 설정으로 등록 |

이 모듈에는 **특정 서비스나 업무에 관한 코드를 넣지 않습니다.**
의존성은 `spring-web`, `spring-webmvc`, `jackson-databind`, `jakarta.validation-api`, `spring-boot-autoconfigure`, `slf4j-api`로 한정합니다.
나중에 다른 프로젝트에서도 쓸 수 있도록 별도 라이브러리로 분리할 수 있게 하기 위해서입니다.

---

## 6. 변경 규칙

이 형식은 서비스 사이의 계약입니다. 바꿀 때는 호환성을 먼저 판단합니다.

| 변경 | 호환 | 라이브러리로 분리한 뒤의 버전 |
|---|---|---|
| 응답 필드 삭제, 이름 변경, 타입 변경 | **깨짐** | 메이저 (`2.0.0`) |
| HTTP 상태 정책 변경 | **깨짐** | 메이저 |
| 기존 코드의 의미 변경 | **깨짐** | 메이저. 하지 않습니다 |
| `COMMON` 코드 추가 | 유지 | 마이너 (`1.1.0`) |
| `message` 문구 수정 | 유지 | 패치 (`1.0.1`) |

호환이 깨지는 변경은 이 문서를 먼저 고치고 팀 합의를 거친 뒤 코드에 반영합니다.
