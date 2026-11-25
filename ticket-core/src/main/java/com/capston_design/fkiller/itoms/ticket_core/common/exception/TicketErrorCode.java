package com.capston_design.fkiller.itoms.ticket_core.common.exception;

import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 접두어 TICKET은 임시 값이다. (common-response README 3. 결과 코드의 추가 고려사항)
@Getter
@RequiredArgsConstructor
public enum TicketErrorCode implements BaseErrorCode {

    INVALID_TICKET(HttpStatus.BAD_REQUEST, "TICKET4001", "유효하지 않은 티켓을 요청하였습니다"),
    UNDEFINED_TICKET_STATUS(HttpStatus.BAD_REQUEST, "TICKET4002", "정의되지 않은 TicketStatus 코드명입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
