package com.example.taskmanager.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtBody {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
