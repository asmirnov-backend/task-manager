package com.example.taskmanager.auth.dto;

import com.example.taskmanager.user.Password;
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
    private String firstName;

    @NotNull
    @Size(max = 127)
    private String lastName;

    @Password
    private String password;
}
