package com.example.taskmanager.user;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class UserFactory {

    public User testUser(Set<Role> roles) {
        return new User(UUID.randomUUID(),
                "test@test.ru",
                "test",
                "Ivan",
                "Solok",
                new BCryptPasswordEncoder().encode("123456"),
                new Date(),
                new Date(),
                roles,
                null);
    }
}
