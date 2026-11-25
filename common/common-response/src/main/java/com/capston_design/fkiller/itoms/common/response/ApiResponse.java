package com.capston_design.fkiller.itoms.common.response;

import com.capston_design.fkiller.itoms.common.response.code.BaseErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

/**
 * 표준 응답 형식. 명세는 common-response/README.md 참고.
 */
@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private static final String SUCCESS_CODE = "COMMON200";
    private static final String SUCCESS_MESSAGE = "성공입니다.";

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    // 다른 서비스 응답을 역직렬화할 때 쓴다. 이 모듈은 -parameters 없이 컴파일되므로 이름을 명시한다.
    @JsonCreator
    public ApiResponse(@JsonProperty("isSuccess") Boolean isSuccess,
                       @JsonProperty("code") String code,
                       @JsonProperty("message") String message,
                       @JsonProperty("result") T result) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

    public static <T> ApiResponse<T> onFailure(BaseErrorCode errorCode, T result) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T result) {
        return new ApiResponse<>(false, code, message, result);
    }
}
