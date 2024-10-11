package com.technokratos.tests_meet_up.exception.model;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private int statusCode;
    private String message;

    public BaseException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
