package com.example.taskmanager.task;

import com.example.taskmanager.user.User;

import java.util.Date;
import java.util.UUID;

public class TaskFactory {

    public Task forTests(User creator) {
        return new Task(UUID.randomUUID(), "test name", "test description", TaskStatus.OPEN, new Date(), new Date(), creator);
    }
}
