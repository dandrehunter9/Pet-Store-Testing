package com.petstoreservices.exceptions;

/**
 * Exception if any of the body data enum types aren't what expected
 */
public class PetStoreAnimalTypeException extends Exception {
    public PetStoreAnimalTypeException(String errorMessage) {
        super(errorMessage);
    }}
