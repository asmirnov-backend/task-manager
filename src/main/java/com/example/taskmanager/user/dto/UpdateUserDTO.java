package com.example.taskmanager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDTO {

    @NotNull
    @NotBlank
    @Size(max = 127)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 127)
    private String lastName;
}
