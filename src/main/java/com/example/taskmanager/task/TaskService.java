package com.example.taskmanager.task;

import com.example.taskmanager.task.dto.TaskCreateDTO;
import com.example.taskmanager.task.dto.TaskUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public Task createTask(TaskCreateDTO taskCreateDTO) {
        Task task = modelMapper.map(taskCreateDTO, Task.class);
        return taskRepository.save(task);
    }

    public Task updateTask(UUID id, TaskUpdateDTO task_update_DTO) throws NotFoundException {
        Task current_task = getTaskById(id);

        BeanUtils.copyProperties(task_update_DTO, current_task, "id"); // Копируем свойства из обновленной задачи в существующую, игнорируя id
        return taskRepository.save(current_task); // Сохраняем обновленную задачу и возвращаем ее
    }

    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(UUID taskId) throws NotFoundException {
        log.info("test logger uuid: {}", taskId);
        return taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
}
