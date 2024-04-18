package com.example.taskmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);
}
