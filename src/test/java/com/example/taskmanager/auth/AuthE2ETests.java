package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDto;
import com.example.taskmanager.auth.dto.RegistrationDto;
import com.example.taskmanager.auth.exception.IncorrectCredentialsException;
import com.example.taskmanager.user.*;
import com.example.taskmanager.user.exception.UserAlreadyExistByEmailException;
import com.example.taskmanager.user.exception.UserAlreadyExistByUsernameException;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        LoginDto loginDTO = new LoginDto(user.getEmail(), "123456");

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
        LoginDto loginDTO = new LoginDto("notExistEmail", "123456");

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(IncorrectCredentialsException.class));
    }

    @Test
    void registration_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        RegistrationDto registrationDTO = new RegistrationDto("test@test.ru", "test", "Andrew", "Smirnov", "ri2u34fi3f43");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());

        mvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(new LoginDto(registrationDTO.getEmail(), registrationDTO.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void registration_badRequest() throws Exception {
        RegistrationDto registrationDTO = new RegistrationDto("test@test.ru", null, "Andrew", "Smirnov", "ri");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void registration_UserAlreadyExistByUsernameException() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = userRepository.save(new UserFactory().testUser(Collections.singleton(role)));
        RegistrationDto registrationDTO = new RegistrationDto("f32dw@wq12e.ru", user.getUsername(), "Andrew", "Smirnov", "ri");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserAlreadyExistByUsernameException.class));
    }

    @Test
    void registration_UserAlreadyExistByEmailException() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = userRepository.save(new UserFactory().testUser(Collections.singleton(role)));
        RegistrationDto registrationDTO = new RegistrationDto(user.getEmail(), "d1221d2q3ew", "Andrew", "Smirnov", "ri");

        mvc.perform(post("/auth/registration")
                        .content(objectMapper.writeValueAsString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(UserAlreadyExistByEmailException.class));
    }
}
