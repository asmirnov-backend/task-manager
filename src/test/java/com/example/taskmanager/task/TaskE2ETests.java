package com.example.taskmanager.task;

import com.example.taskmanager.auth.JwtProvider;
import com.example.taskmanager.task.dto.TaskCreateDTO;
import com.example.taskmanager.task.dto.TaskCreateDTOFactory;
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
class TaskE2ETests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    void getTaskById_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        Task task = taskRepository.save(new TaskFactory().forTests(user));
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(get("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId().toString()))
                .andExpect(jsonPath("$.name").value(task.getName()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void getTaskById_okForAdmin() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        Task task = taskRepository.save(new TaskFactory().forTests(user));

        Role roleAdmin = new RoleFactory().roleAdmin();
        roleRepository.save(roleAdmin);
        User admin = new UserFactory().testUser(new HashSet<>(Collections.singleton(roleAdmin)));
        userRepository.save(admin);
        String accessToken = jwtProvider.generateAccessToken(admin);

        mvc.perform(get("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId().toString()))
                .andExpect(jsonPath("$.name").value(task.getName()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void getTaskById_forbiddenByUnauthorize() throws Exception {
        mvc.perform(get("/tasks/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getTaskById_forbiddenByCreator() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);
        Task task = taskRepository.save(new TaskFactory().forTests(creator));

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(get("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllTasks_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Task[] tasks = {new TaskFactory().forTests(user), new TaskFactory().forTests(user)};
        taskRepository.saveAll(Arrays.asList(tasks));

        mvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void getAllTasks_emptyBecauseCreatorIsAnotherUser() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        Task[] tasks = {new TaskFactory().forTests(creator), new TaskFactory().forTests(creator)};
        taskRepository.saveAll(Arrays.asList(tasks));

        mvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void createTask_created() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        TaskCreateDTO taskCreateDTO = new TaskCreateDTOFactory().forTests();
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(taskCreateDTO.getName()));
    }

    @Test
    void createTask_badRequest() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        TaskCreateDTO taskCreateDTO = new TaskCreateDTOFactory().forTests();
        taskCreateDTO.setName(null);

        mvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Task task = taskRepository.save(new TaskFactory().forTests(user));
        task.setName("New updated name");

        mvc.perform(put("/tasks/{id}", task.getId())
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    void updateTask_notFound() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Task task = new TaskFactory().forTests(user);

        mvc.perform(put("/tasks/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        Task task = taskRepository.save(new TaskFactory().forTests(user));

        mvc.perform(delete("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(task.getId()));
    }

    @Test
    void deleteTask_throwNotCreatorException() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User creator = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(creator);

        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        String accessToken = jwtProvider.generateAccessToken(user);

        Task task = taskRepository.save(new TaskFactory().forTests(creator));

        mvc.perform(delete("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                )
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotCreatorException.class));
    }
}
