package com.example.taskmanager.common.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }
}
