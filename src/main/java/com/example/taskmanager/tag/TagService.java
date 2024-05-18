package com.example.taskmanager.tag;

import com.example.taskmanager.exception.NotCreatorException;
import com.example.taskmanager.tag.dto.TagCreateDto;
import com.example.taskmanager.tag.dto.TagInPageDto;
import com.example.taskmanager.tag.dto.TagUpdateDto;
import com.example.taskmanager.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public Tag getTagById(UUID tagId) throws NotFoundException {
        return tagRepository.findById(tagId).orElseThrow(NotFoundException::new);
    }

    public Page<TagInPageDto> getAllTagsByCreatorId(Pageable pageable, UUID creatorId) {
        return tagRepository.findAllByCreator_Id(pageable, creatorId);
    }


    public Tag createTag(TagCreateDto tagCreateDTO, User creator) {
        Tag tag = modelMapper.map(tagCreateDTO, Tag.class);
        tag.setId(UUID.randomUUID());
        tag.setCreator(creator);
        return tagRepository.save(tag);
    }

    public Tag updateTag(UUID id, TagUpdateDto tagUpdateDTO) throws NotFoundException {
        Tag currentTag = getTagById(id);

        BeanUtils.copyProperties(tagUpdateDTO, currentTag, "id"); // Копируем свойства из обновленной задачи в существующую, игнорируя id
        return tagRepository.save(currentTag); // Сохраняем обновленную задачу и возвращаем ее
    }

    public void deleteTagWithCreatorCheck(UUID tagId, UUID creatorId) throws NotCreatorException {
        Optional<Tag> tag = tagRepository.findById(tagId);

        if (tag.isPresent()) {
            if (tag.get().getCreator().getId().equals(creatorId)) {
                tagRepository.deleteById(tagId);
            } else {
                throw new NotCreatorException();
            }
        }
    }
}
