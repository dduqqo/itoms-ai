package com.capston_design.fkiller.itoms.common.response.exception;

import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import lombok.Getter;

/**
 * 업무 예외. detail은 응답의 result에 그대로 담긴다.
 */
@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;
    private final Object detail;

    public GeneralException(BaseErrorCode errorCode) {
        this(errorCode, null);
    }

    public GeneralException(BaseErrorCode errorCode, Object detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
