package com.example.taskmanager.task;

import com.example.taskmanager.task.dto.TaskCreateDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.modelmapper.ModelMapper;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    public Task createTask(TaskCreateDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        return taskRepository.save(task);
    }

    public Task updateTask(UUID id, Task new_task) throws NotFoundException {
        Task current_task = getTaskById(id);

        BeanUtils.copyProperties(new_task, current_task, "id"); // Копируем свойства из обновленной задачи в существующую, игнорируя id
        return taskRepository.save(current_task); // Сохраняем обновленную задачу и возвращаем ее
    }

    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(UUID taskId) throws NotFoundException {
        return taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
    }

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
}
