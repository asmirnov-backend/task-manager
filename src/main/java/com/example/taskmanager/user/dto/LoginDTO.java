package com.example.taskmanager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
