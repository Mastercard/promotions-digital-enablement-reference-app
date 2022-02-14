package com.mastercard.developer.exception;

import java.util.Arrays;

public enum ErrorCodes {
    VALIDATION_ERROR("VALIDATION_ERROR"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    INVALID_INPUT("INVALID_INPUT"),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND");

    public final String code;

    ErrorCodes(final String code) {
        this.code = code;
    }

    public static ErrorCodes getErrorCode(String code) {
        return Arrays.stream(values()).filter(value -> value.code.equals(code)).findFirst().orElse(ErrorCodes.INTERNAL_SERVER_ERROR);
    }

    public static ErrorCodes getErrorSource(String code) {
        if (ErrorCodes.INVALID_INPUT.code.equals(code)) {
            return ErrorCodes.VALIDATION_ERROR;
        } else if (ErrorCodes.ENTITY_NOT_FOUND.code.equals(code)) {
            return ErrorCodes.ENTITY_NOT_FOUND;
        } else if (ErrorCodes.INTERNAL_SERVER_ERROR.code.equals(code)) {
            return ErrorCodes.INTERNAL_SERVER_ERROR;
        } else {
            return ErrorCodes.INVALID_INPUT;
        }
    }

    @Override
    public String toString() {
        return code;
    }
}