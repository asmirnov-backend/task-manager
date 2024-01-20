package com.example.demo.task;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(UUID taskId, Task task) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    public void deleteTask(UUID taskId) {
        // Удаление задачи по идентификатору
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(UUID taskId) throws NotFoundException {
        // Получение задачи по идентификатору
        return taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
    }

    public List<Task> getAllTasks() {
        // Получение всех задач из репозитория
        return taskRepository.findAll();
    }
}
