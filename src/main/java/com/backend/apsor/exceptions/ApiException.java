package com.backend.apsor.exceptions;

import com.backend.apsor.enums.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public ApiException(HttpStatus status, ApiErrorCode code, String message) {
        super(message);
        this.status = status;
        this.code = code.getCode();
    }

    public static ApiException conflict(ApiErrorCode code, String message, Object... args) {
        return new ApiException(HttpStatus.CONFLICT, code, String.format(message, args));
    }

    public static ApiException badRequest(ApiErrorCode code, String message, Object... args) {
        return new ApiException(HttpStatus.BAD_REQUEST, code, String.format(message, args));
    }

    public static ApiException notFound(ApiErrorCode code, String message, Object... args) {
        return new ApiException(HttpStatus.NOT_FOUND, code, String.format(message, args));
    }

    public static ApiException unauthorized(ApiErrorCode code, String message, Object... args) {
        return new ApiException(HttpStatus.UNAUTHORIZED, code, String.format(message, args));
    }

    public static ApiException forbidden(ApiErrorCode code, String message, Object... args) {
        return new ApiException(HttpStatus.FORBIDDEN, code, String.format(message, args));
    }


}
