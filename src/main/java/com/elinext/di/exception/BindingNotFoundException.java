package com.elinext.di.exception;

public class BindingNotFoundException extends RuntimeException{
    public BindingNotFoundException(String message) {
        super(message);
    }
}
