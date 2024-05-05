package com.example.taskmanager.auth.dto;

import com.example.taskmanager.user.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotNull
    @NotBlank
    private String email;

    @Password
    private String password;
}
