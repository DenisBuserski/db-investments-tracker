package com.investments.tracker.controller.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${application.error.response.status.url}")
    private String responseStatusUrl;

    @Value("${application.error.bad.request.url}")
    private String badRequestUrl;

    @Value("${application.error.not.found.url}")
    private String notFoundUrl;

    @Value("${application.error.internal.server.error.url}")
    private String internalServerErrorUrl;


    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request) {
        log.error("Handled ResponseStatusException", exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(exception.getStatusCode());
        problemDetail.setTitle("Request failed");
        problemDetail.setDetail(exception.getReason());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(responseStatusUrl + exception.getStatusCode().value()));
        return problemDetail;
    }

    // Handle HTTP 400
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException exception, HttpServletRequest request) {
        log.warn("Bad request: {}", exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad request");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(badRequestUrl));
        return problemDetail;
    }

    // Handle HTTP 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        log.warn("Resource not found: {}", exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Resource not found");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(notFoundUrl));
        return problemDetail;
    }

    // Handle HTTP 500
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected error", exception);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An unexpected error occurred. Please contact support.");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(internalServerErrorUrl));
        return problemDetail;
    }

}

