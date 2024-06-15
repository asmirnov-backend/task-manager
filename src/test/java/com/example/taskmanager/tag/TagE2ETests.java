package com.example.taskmanager.tag;

import com.example.taskmanager.auth.JwtProvider;
import com.example.taskmanager.common.exception.NotCreatorException;
import com.example.taskmanager.tag.dto.TagCreateDto;
import com.example.taskmanager.tag.dto.TagCreateDtoFactory;
import com.example.taskmanager.task.Task;
import com.example.taskmanager.task.TaskFactory;
import com.example.taskmanager.task.TaskRepository;
import com.example.taskmanager.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagE2ETests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void contextLoads() {
    }

    @Test
    void getTagById_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        Task task = taskRepository.save(new TaskFactory().forTests(user));
        Tag tag = tagRepository.save(new TagFactory().forTests(user, task));
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(get("/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag.getId().toString()))
                .andExpect(jsonPath("$.name").value(tag.getName()))
                .andExpect(jsonPath("$.color").value(tag.getColor()));
    }

    @Test
    void getTagById_okForAdmin() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        Tag tag = tagRepository.save(new TagFactory().forTests(user));

        Role roleAdmin = new RoleFactory().roleAdmin();
        roleRepository.save(roleAdmin);
        User admin = new UserFactory().testUser(new HashSet<>(Collections.singleton(roleAdmin)));
        userRepository.save(admin);
        String accessToken = jwtProvider.generateAccessToken(admin);

        mvc.perform(get("/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag.getId().toString()))
                .andExpect(jsonPath("$.name").value(tag.getName()))
                .andExpect(jsonPath("$.color").value(tag.getColor()));
    }

    @Test
    void getTagById_forbiddenByUnauthorized() throws Exception {
        mvc.perform(get("/tags/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getTagById_forbiddenByCreator() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);
        Tag tag = tagRepository.save(new TagFactory().forTests(creator));

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(get("/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllTags_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag[] tags = {new TagFactory().forTests(user), new TagFactory().forTests(user)};
        tagRepository.saveAll(Arrays.asList(tags));

        mvc.perform(get("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void getAllTags_emptyBecauseCreatorIsAnotherUser() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag[] tags = {new TagFactory().forTests(creator), new TagFactory().forTests(creator)};
        tagRepository.saveAll(Arrays.asList(tags));

        mvc.perform(get("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void createTag_created() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        TagCreateDto tagCreateDTO = new TagCreateDtoFactory().forTests();
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(post("/tags")
                        .content(objectMapper.writeValueAsString(tagCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(tagCreateDTO.getName()));
    }

    @Test
    void createTag_badRequest() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        TagCreateDto tagCreateDTO = new TagCreateDtoFactory().forTests();
        tagCreateDTO.setName(null);

        mvc.perform(post("/tags")
                        .content(objectMapper.writeValueAsString(tagCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTag_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag tag = tagRepository.save(new TagFactory().forTests(user));
        tag.setName("New updated name");

        mvc.perform(put("/tags/{id}", tag.getId())
                        .content(objectMapper.writeValueAsString(tag))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(tag.getName()));
    }

    @Test
    void updateTag_notFound() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag tag = new TagFactory().forTests(user);

        mvc.perform(put("/tags/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(tag))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTag_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag tag = tagRepository.save(new TagFactory().forTests(user));

        mvc.perform(delete("/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(tagRepository.existsById(tag.getId()));
    }

    @Test
    void deleteTag_NotCreatorException() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        Tag tag = tagRepository.save(new TagFactory().forTests(creator));

        mvc.perform(delete("/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotCreatorException.class));
    }
}
