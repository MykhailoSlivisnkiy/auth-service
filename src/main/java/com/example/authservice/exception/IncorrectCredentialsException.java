package com.example.authservice.exception;

public class IncorrectCredentialsException extends Exception  {
    public IncorrectCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}