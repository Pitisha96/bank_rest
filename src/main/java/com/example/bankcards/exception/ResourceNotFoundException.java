package com.example.bankcards.exception;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
