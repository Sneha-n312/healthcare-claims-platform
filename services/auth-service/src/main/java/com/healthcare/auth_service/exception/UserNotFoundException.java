package com.healthcare.auth_service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("Username " + username + " not found");
    }
}
