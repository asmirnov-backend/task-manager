package com.example.taskmanager.tag;

import com.example.taskmanager.auth.AuthorizeByCreatorOrAdmin;
import com.example.taskmanager.auth.JwtAuthentication;
import com.example.taskmanager.common.exception.NotCreatorException;
import com.example.taskmanager.tag.dto.TagCreateDto;
import com.example.taskmanager.tag.dto.TagDto;
import com.example.taskmanager.tag.dto.TagInPageDto;
import com.example.taskmanager.tag.dto.TagUpdateDto;
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
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @AuthorizeByCreatorOrAdmin
    public TagDto getTagDtoById(@PathVariable(name = "id") UUID id) throws NotFoundException {
        return tagService.getTagDtoById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @PageableAsQueryParam
    public Page<TagInPageDto> getAllTags(@ParameterObject Pageable pageable, JwtAuthentication authentication) {
        return tagService.getAllTagsByCreatorId(pageable, authentication.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public Tag createTag(@Valid @RequestBody TagCreateDto TagCreateDTO, JwtAuthentication authentication) {
        return tagService.createTag(TagCreateDTO, authentication.getUserReference());
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @AuthorizeByCreatorOrAdmin
    @Transactional
    public Tag updateTag(@PathVariable(name = "id") UUID id, @Valid @RequestBody TagUpdateDto tag) throws NotFoundException {
        return tagService.updateTag(id, tag);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void deleteTag(@PathVariable(name = "id") UUID id, JwtAuthentication authentication) throws NotCreatorException {
        tagService.deleteTagWithCreatorCheck(id, authentication.getId());
    }
}
