package com.example.taskmanager.task;

import jakarta.validation.Valid;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("{id}")
    public Task getTaskById(@PathVariable(name = "id") UUID id) throws NotFoundException {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("{id}")
    public Task updateTask(@PathVariable(name = "id") UUID id, @Valid @RequestBody Task task) throws NotFoundException {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable(name = "id") UUID id) {
        taskService.deleteTask(id);
    }
}
