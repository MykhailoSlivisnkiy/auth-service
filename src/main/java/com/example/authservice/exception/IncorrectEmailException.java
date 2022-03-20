package com.example.authservice.exception;

public class IncorrectEmailException extends Exception  {
    public IncorrectEmailException(String errorMessage) {
        super(errorMessage);
    }
}