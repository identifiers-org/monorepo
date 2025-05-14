package org.identifiers.satellite.frontend.satellitewebspa.api.controllers;

import org.identifiers.satellite.frontend.satellitewebspa.api.exceptions.FailedResolutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ResponseEntityExceptionHandler extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = { Exception.class })
    protected ModelAndView handleConflict(Exception ex, HttpServletResponse response) {
        log.error("Unforeseen exception", ex);
        response.addCookie(new Cookie("message", URLEncoder.encode(ex.getMessage(), UTF_8)));
        return new ModelAndView("/index.html", INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { FailedResolutionException.class })
    protected ModelAndView failedResolution(FailedResolutionException ex, HttpServletResponse response) {
        log.debug("failedResolutionException", ex);
        String encodedMessage = URLEncoder.encode(ex.getMessage(), UTF_8);
        String cookieVal = String.format("message=%s; SameSite=Lax", encodedMessage);
        response.addHeader("Set-Cookie", cookieVal);
        response.addHeader("Set-Cookie", "title=Resolution+failed; SameSite=Lax");
        return new ModelAndView("/index.html", NOT_FOUND);
    }
}
