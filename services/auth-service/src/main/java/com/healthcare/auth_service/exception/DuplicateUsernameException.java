package com.healthcare.auth_service.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String message) {
        super("Username " + message + " already exists");
    }
}
