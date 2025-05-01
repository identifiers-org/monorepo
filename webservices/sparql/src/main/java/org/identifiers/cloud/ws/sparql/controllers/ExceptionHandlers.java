package org.identifiers.cloud.ws.sparql.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class ExceptionHandlers {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ServiceResponse<Void> handleExceptionAsEndpointResponse(Throwable e) {
        log.error(e.getMessage(), e);
        return ServiceResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
