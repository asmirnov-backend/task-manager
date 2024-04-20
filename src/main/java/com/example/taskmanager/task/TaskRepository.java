package com.example.taskmanager.task;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false) // exported = true создаст crud эндпоинты для сущности
public interface TaskRepository extends JpaRepository<Task, UUID> {
    // Можно добавить специфичные методы запросов, если нужно
}
