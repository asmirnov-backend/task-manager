package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TaskInPageDTO {
    private UUID id;
    private String name;
    private String description;
    private TaskStatus status;
    private Date createdAt;
    private Date updatedAt;
}
