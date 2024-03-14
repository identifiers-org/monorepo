package org.identifiers.cloud.hq.ws.registry.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.identifiers.cloud.hq.ws.registry.api.ApiCentral;
import org.identifiers.cloud.hq.ws.registry.api.responses.ServiceResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static String now() {
        // Date for easier finding related entries in log files
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleResourceNotFound (ResourceNotFoundException ex, WebRequest request) {
        ServiceResponse<Object> responseBody = ServiceResponse.getBaseResponse()
                .setApiVersion(ApiCentral.apiVersion)
                .setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        // Last hope for logging of unforeseen errors
        // Also a way to make all responses to be of type ServiceResponse
        log.error("Unforeseen exception", ex);
        ServiceResponse<Object> responseBody = ServiceResponse.getBaseResponse()
                .setApiVersion(ApiCentral.apiVersion)
                .setErrorMessage(String.format("Unforeseen exception at %s: %s", now(), ex.getMessage()));
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
