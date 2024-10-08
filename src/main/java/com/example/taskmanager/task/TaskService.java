package com.example.taskmanager.task;

import com.example.taskmanager.common.exception.NotCreatorException;
import com.example.taskmanager.task.dto.TaskCreateDto;
import com.example.taskmanager.task.dto.TaskDto;
import com.example.taskmanager.task.dto.TaskInPageDto;
import com.example.taskmanager.task.dto.TaskUpdateDto;
import com.example.taskmanager.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public Task getTaskById(UUID taskId) throws NotFoundException {
        log.info("test logger uuid: {}", taskId);
        return taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
    }

    public TaskDto getTaskDtoById(UUID taskId) throws NotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(NotFoundException::new);
        return modelMapper.map(task, TaskDto.class);
    }

    public Page<TaskInPageDto> getAllTasksByCreatorId(Pageable pageable, UUID creatorId) {
        return taskRepository.findAllByCreator_Id(pageable, creatorId);
    }

    @Transactional
    public Task createTask(TaskCreateDto taskCreateDto, User creator) {
        Task task = modelMapper.map(taskCreateDto, Task.class);
        task.setId(UUID.randomUUID());
        task.setCreator(creator);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(UUID id, TaskUpdateDto taskUpdateDto) throws NotFoundException {
        Task currentTask = getTaskById(id);

        BeanUtils.copyProperties(taskUpdateDto, currentTask, "id"); // Копируем свойства из обновленной задачи в существующую, игнорируя id
        return taskRepository.save(currentTask); // Сохраняем обновленную задачу и возвращаем ее
    }

    @Transactional
    public void deleteTaskWithCreatorCheck(UUID taskId, UUID creatorId) throws NotCreatorException {
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isPresent()) {
            if (task.get().getCreator().getId().equals(creatorId)) {
                taskRepository.deleteById(taskId);
            } else {
                throw new NotCreatorException();
            }
        }
    }
}
