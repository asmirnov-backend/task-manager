package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskStatus;


public class TaskCreateDtoFactory {

    public TaskCreateDto forTests() {
        return new TaskCreateDto("test name", "test description", TaskStatus.OPEN);
    }
}
