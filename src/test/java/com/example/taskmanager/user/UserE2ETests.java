package com.example.taskmanager.user;

import com.example.taskmanager.auth.JwtProvider;
import com.example.taskmanager.user.dto.ChangePasswordDto;
import com.example.taskmanager.user.dto.UpdateUserDto;
import com.example.taskmanager.user.exception.CurrentPasswordIsIncorrectException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserE2ETests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void getCurrentUser() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        mvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", String.format("Bearer %s", accessToken)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()));
    }

    @Test
    void updateCurrentUser() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        UpdateUserDto updateUserDTO = new UpdateUserDto("newName", "newSurname");

        mvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO))
                        .header("Authorization", String.format("Bearer %s", accessToken)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(updateUserDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateUserDTO.getLastName()));
    }

    @Test
    void changePasswordForCurrentUser_ok() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        ChangePasswordDto changePasswordDTO = new ChangePasswordDto("123456", "12345678");

        mvc.perform(patch("/users/me/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDTO))
                        .header("Authorization", String.format("Bearer %s", accessToken)))
                .andDo(print())
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).get();
        assertTrue(passwordEncoder.matches(changePasswordDTO.getNewPassword(), updatedUser.getPassword()));
    }

    @Test
    void changePasswordForCurrentUser_CurrentPasswordIsIncorrectException() throws Exception {
        Role role = new RoleFactory().roleUser();
        roleRepository.save(role);
        User user = new UserFactory().testUser(new HashSet<>(Collections.singleton(role)));
        userRepository.save(user);
        String accessToken = jwtProvider.generateAccessToken(user);

        ChangePasswordDto changePasswordDTO = new ChangePasswordDto("923hd3uf2198o312wq", "12345678");

        mvc.perform(patch("/users/me/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDTO))
                        .header("Authorization", String.format("Bearer %s", accessToken)))
                .andDo(print())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(CurrentPasswordIsIncorrectException.class));
    }

}
