package com.example.taskmanager.auth.exception;

import jakarta.security.auth.message.AuthException;

public class IncorrectCredentialsException extends AuthException {
    public IncorrectCredentialsException() {
        super("Login or password is incorrect");
    }
}
