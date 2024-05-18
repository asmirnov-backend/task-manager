package com.example.taskmanager.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagCreateDto {
    @NotNull
    @Size(max = 63)
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 63)
    @NotBlank
    private String color;
}
