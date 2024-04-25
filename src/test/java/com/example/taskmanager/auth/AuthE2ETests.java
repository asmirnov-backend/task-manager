package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDTO;
import com.example.taskmanager.auth.dto.RegistrationDTO;
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

import java.util.Collections;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthE2ETests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void login_ok() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        User user = new UserFactory().test_user(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        LoginDTO loginDTO = new LoginDTO(user.getEmail(), "123456");

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void login_IncorrectCredentialsException() throws Exception {
        LoginDTO loginDTO = new LoginDTO("notExistEmail", "123456");

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registration_ok() throws Exception {
        Role role = new RoleFactory().role_user();
        roleRepository.save(role);
        RegistrationDTO registrationDTO = new RegistrationDTO("test@test.ru", "test", "Andrew", "Smirnov","ri2u34fi3f43");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginDTO(registrationDTO.getEmail(), registrationDTO.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void registration_badRequest() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@test.ru", null, "Andrew", "Smirnov","ri");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
