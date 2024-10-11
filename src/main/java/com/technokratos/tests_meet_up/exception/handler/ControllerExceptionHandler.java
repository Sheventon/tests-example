package com.technokratos.tests_meet_up.exception.handler;

import com.technokratos.tests_meet_up.exception.model.BaseException;
import com.technokratos.tests_meet_up.exception.utils.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<ErrorMessage> returnErrorMessage(BaseException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ErrorMessage.builder()
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .statusCode(ex.getStatusCode())
                        .message(ex.getMessage())
                        .build());
    }
}
