package com.example.taskmanager.task.dto;

import com.example.taskmanager.tag.dto.TagDto;
import com.example.taskmanager.task.TaskStatus;
import com.example.taskmanager.user.dto.UserWithOnlyIdDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private UUID id;
    private String name;
    private String description;
    private TaskStatus status;
    private Date createdAt;
    private Date updatedAt;
    private UserWithOnlyIdDto creator;
    private Set<TagDto> tags;
}
