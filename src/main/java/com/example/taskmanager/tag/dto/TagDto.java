package com.example.taskmanager.tag.dto;

import com.example.taskmanager.user.dto.UserWithOnlyIdDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private UUID id;
    private String name;
    private String color;
    private UserWithOnlyIdDto creator;
}
