package com.mastercard.developer.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequest extends RuntimeException {
    private final String code;

    public InvalidRequest(String message) {
        super(message);
        this.code = null;
    }

    public InvalidRequest(HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.code = null;
    }

    public InvalidRequest(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
