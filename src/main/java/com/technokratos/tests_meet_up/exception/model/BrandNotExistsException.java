package com.technokratos.tests_meet_up.exception.model;

import org.springframework.http.HttpStatus;

public class BrandNotExistsException extends BaseException {

    public BrandNotExistsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
