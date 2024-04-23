package com.example.taskmanager.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDTO {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3, max = 63)
    private String username;

    @NotNull
    @Size(max = 127)
    private String name;

    @NotNull
    @Size(max = 127)
    private String surname;

    @NotNull
    @Size(min = 6)
    private String password;
}
