package com.example.taskmanager.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokensDTO {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
