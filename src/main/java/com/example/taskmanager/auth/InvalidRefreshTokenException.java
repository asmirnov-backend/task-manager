package com.example.taskmanager.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRefreshTokenException extends Error {
    InvalidRefreshTokenException() {
        super("RefreshToken is invalid");
    }
}
