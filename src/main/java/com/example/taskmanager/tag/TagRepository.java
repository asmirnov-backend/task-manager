package com.example.taskmanager.tag;


import com.example.taskmanager.tag.dto.TagInPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false) // exported = true создаст crud эндпоинты для сущности
public interface TagRepository extends JpaRepository<Tag, UUID> {
    Page<TagInPageDto> findAllByCreator_Id(Pageable pageable, UUID creatorId);
}
