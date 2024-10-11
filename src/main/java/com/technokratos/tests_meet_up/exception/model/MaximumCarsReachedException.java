package com.technokratos.tests_meet_up.exception.model;

import org.springframework.http.HttpStatus;

public class MaximumCarsReachedException extends BaseException {

    public MaximumCarsReachedException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
