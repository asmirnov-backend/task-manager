package com.example.taskmanager.task;

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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void contextLoads() {
    }

    @Test
    void getTaskById() throws Exception {
        Task task = taskRepository.save(new TaskFactory().createSimpleTestTask());

        mvc.perform(get("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId().toString()))
                .andExpect(jsonPath("$.name").value(task.getName()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void getAllTasks() throws Exception {
        Task[] tasks = {new TaskFactory().createSimpleTestTask(), new TaskFactory().createSimpleTestTask()};
        taskRepository.saveAll(Arrays.asList(tasks));

        mvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void createTask_ok() throws Exception {
        Task task = new TaskFactory().createSimpleTestTask();

        mvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    void createTask_badRequest() throws Exception {
        Task task = new TaskFactory().createSimpleTestTask();
        task.setName(null);

        mvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ok() throws Exception {
        Task task = taskRepository.save(new TaskFactory().createSimpleTestTask());
        task.setName("New updated name");

        mvc.perform(put("/tasks/{id}", task.getId())
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(task.getName()));
    }

    @Test
    void updateTask_notFound() throws Exception {
        Task task = new TaskFactory().createSimpleTestTask();

        mvc.perform(put("/tasks/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(task))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask() throws Exception {
        Task task = taskRepository.save(new TaskFactory().createSimpleTestTask());

        mvc.perform(delete("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(task.getId()));
    }
}
