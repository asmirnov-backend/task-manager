package com.example.taskmanager.tag;

import com.example.taskmanager.task.Task;
import com.example.taskmanager.user.User;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class TagFactory {

    public Tag forTests(User creator, Task task) {
        return new Tag(UUID.randomUUID(), "test name", "test description", new Date(), new Date(), creator, Collections.singleton(task));
    }

    public Tag forTests(User creator) {
        return new Tag(UUID.randomUUID(), "test name", "test description", new Date(), new Date(), creator, new HashSet<>());
    }
}
