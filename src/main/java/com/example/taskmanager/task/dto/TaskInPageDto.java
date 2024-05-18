package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TaskInPageDto {
    private UUID id;
    private String name;
    private String description;
    private TaskStatus status;
    private Date createdAt;
    private Date updatedAt;
}
