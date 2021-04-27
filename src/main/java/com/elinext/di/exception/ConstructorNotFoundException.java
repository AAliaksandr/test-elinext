package com.elinext.di.exception;

public class ConstructorNotFoundException extends RuntimeException{
    public ConstructorNotFoundException(String message) {
        super(message);
    }
}
