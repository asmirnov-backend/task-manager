package com.example.taskmanager.auth;

import jakarta.security.auth.message.AuthException;

public class IncorrectCredentialsException extends AuthException {
    IncorrectCredentialsException() {
        super("Login or password is incorrect");
    }
}
