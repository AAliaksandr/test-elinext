package com.elinext.di.exception;

public class NoInjectConstructorException extends RuntimeException{
    public NoInjectConstructorException(String message) {
        super(message);
    }
}
