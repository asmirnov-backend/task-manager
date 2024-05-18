package com.example.taskmanager.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TagInPageDto {
    private UUID id;
    private String name;
}
