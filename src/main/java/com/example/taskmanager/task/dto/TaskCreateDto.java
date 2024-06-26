package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskCreateDto {
    @NotNull
    @Size(max = 255)
    @NotBlank
    private String name;

    @NotNull
    @Size(max = 4095)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status;
}
