package com.example.taskmanager.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshJwtRequestDTO {
    @NotNull
    private String refreshToken;
}
