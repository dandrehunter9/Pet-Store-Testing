package com.petstore.exceptions;

/**
 * Exception if pet type is not supported
 */
public class PetTypeNotSupportedException extends Exception {
    public PetTypeNotSupportedException(String errorMessage) {
        super(errorMessage);
    }}
