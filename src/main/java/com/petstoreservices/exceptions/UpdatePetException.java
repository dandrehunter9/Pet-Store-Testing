package com.petstoreservices.exceptions;

public class UpdatePetException extends Exception {
    public UpdatePetException(String errorMessage) {
        super(errorMessage);
    }
}
