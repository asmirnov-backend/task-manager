package com.example.taskmanager.auth;

public class InvalidRefreshTokenException extends Error {
    InvalidRefreshTokenException() {
        super("Refresh token is invalid");
    }
}
