package com.example.taskmanager.auth.exception;

import jakarta.security.auth.message.AuthException;

public class InvalidRefreshTokenException extends AuthException {
    public InvalidRefreshTokenException() {
        super("Refresh token is invalid");
    }
}
