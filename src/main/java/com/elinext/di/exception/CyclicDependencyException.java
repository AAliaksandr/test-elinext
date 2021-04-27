package com.elinext.di.exception;

public class CyclicDependencyException extends RuntimeException{
    public CyclicDependencyException(String message) {
        super(message);
    }
}
