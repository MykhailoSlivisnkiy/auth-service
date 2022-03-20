package com.example.authservice.exception;

public class UserNotFound extends Exception  {
    public UserNotFound(String errorMessage) {
        super(errorMessage);
    }
}