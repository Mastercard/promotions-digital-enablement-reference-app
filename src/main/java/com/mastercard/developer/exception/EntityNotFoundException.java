package com.mastercard.developer.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String code;

    public EntityNotFoundException(String message) {
        super(message);
        this.code = null;
    }

    public EntityNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}