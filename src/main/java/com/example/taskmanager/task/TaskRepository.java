package com.example.taskmanager.task;


import com.example.taskmanager.task.dto.TaskInPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false) // exported = true создаст crud эндпоинты для сущности
public interface TaskRepository extends JpaRepository<Task, UUID> {
    // Можно добавить специфичные методы запросов, если нужно

    Page<TaskInPageDto> findAllByCreator_Id(Pageable pageable, UUID creatorId);
}
