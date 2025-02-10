package org.identifiers.cloud.ws.sparql.controllers;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.identifiers.cloud.commons.messages.responses.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class ExceptionHandlers {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            MissingRequestValueException.class,
            HttpMediaTypeNotSupportedException.class,
            MalformedQueryException.class
    })
    public ServiceResponse<Void> handleExceptionAsEndpointResponse(Throwable e) {
        return new ServiceResponse<Void>()
                .setHttpStatus(HttpStatus.BAD_REQUEST)
                .setErrorMessage(e.getMessage());
    }
}
