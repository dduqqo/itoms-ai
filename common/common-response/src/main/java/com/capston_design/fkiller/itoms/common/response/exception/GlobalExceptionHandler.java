package com.capston_design.fkiller.itoms.common.response.exception;

import com.capston_design.fkiller.itoms.common.response.ApiResponse;
import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import com.capston_design.fkiller.itoms.common.response.code.CommonErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 모든 예외를 표준 응답 형식으로 바꾼다. CommonResponseAutoConfiguration이 등록한다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException e, WebRequest request) {
        BaseErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(e, ApiResponse.onFailure(errorCode, e.getDetail()),
                HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.merge(violation.getPropertyPath().toString(), violation.getMessage(), (a, b) -> a + ", " + b);
        }
        return badRequest(e, errors, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.merge(fieldError.getField(),
                fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage(), (a, b) -> a + ", " + b));
        return badRequest(e, errors, request);
    }

    // 처리하지 못한 예외: 내부 정보는 로그에만 남기고 응답에는 넣지 않는다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Exception e, WebRequest request) {
        log.error("처리하지 못한 예외", e);
        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(e, ApiResponse.onFailure(errorCode, null),
                HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    // Spring MVC 기본 예외(404, 405, 415, 본문 파싱 실패 등)도 표준 형식으로 감싼다.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        Object apiBody = body instanceof ApiResponse<?> ? body : failureFor(status);
        return super.handleExceptionInternal(e, apiBody, headers, status, request);
    }

    private ResponseEntity<Object> badRequest(Exception e, Map<String, String> errors, WebRequest request) {
        CommonErrorCode errorCode = CommonErrorCode.BAD_REQUEST;
        return handleExceptionInternal(e, ApiResponse.onFailure(errorCode, errors),
                HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }

    private static ApiResponse<Object> failureFor(HttpStatusCode status) {
        return Arrays.stream(CommonErrorCode.values())
                .filter(code -> code.getHttpStatus().value() == status.value())
                .findFirst()
                .map(code -> ApiResponse.onFailure(code, null))
                .orElseGet(() -> {
                    HttpStatus resolved = HttpStatus.resolve(status.value());
                    String message = resolved == null ? "요청을 처리할 수 없습니다." : resolved.getReasonPhrase();
                    return ApiResponse.onFailure("COMMON" + status.value(), message, null);
                });
    }
}
