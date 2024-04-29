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
    void getTaskById() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
    void getAllTasks() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
    void createTask_created() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
    void deleteTask() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
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
}
