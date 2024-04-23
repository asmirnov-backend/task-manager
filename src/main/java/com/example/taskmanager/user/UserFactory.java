package com.example.taskmanager.user;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class UserFactory {

    public User test_user(Set<Role> roles) {
        return new User(UUID.randomUUID(), "test@test.ru", "test", "Ivan", "Solok", new BCryptPasswordEncoder().encode("123456"), new Date(), new Date(), roles);
    }
}
