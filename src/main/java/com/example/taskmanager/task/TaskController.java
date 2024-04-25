package com.example.taskmanager.task;

import com.example.taskmanager.task.dto.TaskCreateDTO;
import com.example.taskmanager.task.dto.TaskUpdateDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public Task getTaskById(@PathVariable(name = "id") UUID id) throws NotFoundException {
        return taskService.getTaskById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Task createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.createTask(taskCreateDTO);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public Task updateTask(@PathVariable(name = "id") UUID id, @Valid @RequestBody TaskUpdateDTO task) throws NotFoundException {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public void deleteTask(@PathVariable(name = "id") UUID id) {
        taskService.deleteTask(id);
    }
}
