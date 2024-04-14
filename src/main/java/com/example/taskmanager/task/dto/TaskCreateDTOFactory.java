package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskStatus;


public class TaskCreateDTOFactory {

    public TaskCreateDTO forTests() {
        return new TaskCreateDTO( "test name", "test description", TaskStatus.OPEN);
    }
}
