package com.elinext.di;

public class TooManyConstructorsException extends RuntimeException{

    public TooManyConstructorsException(String message) {
        super(message);
    }
}
