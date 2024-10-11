package com.technokratos.tests_meet_up.exception.model;

import org.springframework.http.HttpStatus;

public class BrandAlreadyExistsException extends BaseException {

    public BrandAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
