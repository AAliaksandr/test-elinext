package com.elinext.di.exception;

public class TooManyConstructorsException extends RuntimeException{

    public TooManyConstructorsException(String message) {
        super(message);
    }
}
