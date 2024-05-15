package com.petstoreservices.exceptions;

/**
 * Exception if file is not created or updated
 */
public class PetInventoryFileNotCreatedException extends Exception {
    public PetInventoryFileNotCreatedException(String errorMessage) {
        super(errorMessage);
    }}
