package com.example.taskmanager.tag.dto;

public class TagCreateDtoFactory {

    public TagCreateDto forTests() {
        return new TagCreateDto("test name", "test description");
    }
}
