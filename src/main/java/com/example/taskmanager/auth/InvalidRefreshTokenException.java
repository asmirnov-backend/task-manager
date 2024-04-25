package com.example.taskmanager.auth;

import jakarta.security.auth.message.AuthException;

public class InvalidRefreshTokenException extends AuthException {
    InvalidRefreshTokenException() {
        super("Refresh token is invalid");
    }
}
