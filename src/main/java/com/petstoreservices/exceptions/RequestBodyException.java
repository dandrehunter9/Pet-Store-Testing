package com.petstoreservices.exceptions;

/**
 * Exception if any of the body data enum types aren't what expected
 */
public class RequestBodyException extends Exception {
    public RequestBodyException(String errorMessage) {
        super(errorMessage);
    }}
