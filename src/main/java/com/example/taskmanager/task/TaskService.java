package com.example.taskmanager.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(UUID taskId, Task task) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(UUID taskId) throws NotFoundException {
        return taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
