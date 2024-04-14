package com.example.taskmanager.task;

import java.util.Date;
import java.util.UUID;

public class TaskFactory {

    public Task createSimpleTestTask() {
        return new Task(UUID.randomUUID(), "test name", "test description", new Date(), new Date(), TaskStatus.OPEN);
    }
}
