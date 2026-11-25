package com.capston_design.fkiller.itoms.service_request_management.common.exception;

import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 접두어 SR은 임시 값이다. (common-response README 3. 결과 코드의 추가 고려사항)
@Getter
@RequiredArgsConstructor
public enum SrErrorCode implements BaseErrorCode {

    INVALID_TASK(HttpStatus.BAD_REQUEST, "SR4001", "유효하지 않은 Task ID 입니다."),
    INVALID_TICKET(HttpStatus.BAD_REQUEST, "SR4002", "유효하지 않은 티켓ID 입니다."),
    UNDEFINED_TASK_STATUS_NAME(HttpStatus.BAD_REQUEST, "SR4003", "정의되지 않은 TaskStatus 코드명입니다."),
    UNDEFINED_TASK_STATUS_CODE(HttpStatus.BAD_REQUEST, "SR4004", "정의되지 않은 TaskStatus 코드입니다."),
    TASKS_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "SR4005", "해당 티켓에 할당된 작업이 모두 완료되지 않았습니다."),
    NO_ASSIGNED_TASKS(HttpStatus.BAD_REQUEST, "SR4006", "해당 티켓에 할당된 작업이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
