package com.elinext.di;

public class NoInjectConstructorException extends RuntimeException{
    public NoInjectConstructorException(String message) {
        super(message);
    }
}
