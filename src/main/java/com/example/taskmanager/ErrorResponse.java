package com.example.taskmanager;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
