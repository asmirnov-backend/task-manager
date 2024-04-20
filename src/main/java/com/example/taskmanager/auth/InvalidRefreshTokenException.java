package com.example.taskmanager.auth;

public class InvalidRefreshTokenException extends Error {
    InvalidRefreshTokenException() {
        super("RefreshToken is invalid");
    }
}
