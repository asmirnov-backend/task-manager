package com.example.taskmanager.task;

import com.example.taskmanager.auth.AuthorizeByCreatorOrAdmin;
import com.example.taskmanager.auth.JwtAuthentication;
import com.example.taskmanager.common.exception.NotCreatorException;
import com.example.taskmanager.task.dto.TaskCreateDto;
import com.example.taskmanager.task.dto.TaskDto;
import com.example.taskmanager.task.dto.TaskInPageDto;
import com.example.taskmanager.task.dto.TaskUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @AuthorizeByCreatorOrAdmin
    public TaskDto getTaskById(@PathVariable(name = "id") UUID id) throws NotFoundException {
        return taskService.getTaskDtoById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @PageableAsQueryParam
    public Page<TaskInPageDto> getAllTasks(@ParameterObject Pageable pageable, JwtAuthentication authentication) {
        return taskService.getAllTasksByCreatorId(pageable, authentication.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Task createTask(@Valid @RequestBody TaskCreateDto taskCreateDto, JwtAuthentication authentication) {
        return taskService.createTask(taskCreateDto, authentication.getUserReference());
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @AuthorizeByCreatorOrAdmin
    @Transactional
    public Task updateTask(@PathVariable(name = "id") UUID id, @Valid @RequestBody TaskUpdateDto task) throws NotFoundException {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void deleteTask(@PathVariable(name = "id") UUID id, JwtAuthentication authentication) throws NotCreatorException {
        taskService.deleteTaskWithCreatorCheck(id, authentication.getId());
    }
}
