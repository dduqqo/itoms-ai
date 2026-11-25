package com.capston_design.fkiller.itoms.common.response.code;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 인터페이스. 각 서비스는 자기 에러 코드를 이 인터페이스를 구현한 enum으로 정의한다.
 */
public interface BaseErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
