package com.example.taskmanager.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagUpdateDto {
    @NotNull
    @Size(max = 63)
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 63)
    @NotBlank
    private String color;
}
