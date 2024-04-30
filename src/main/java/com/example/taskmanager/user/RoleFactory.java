package com.example.taskmanager.user;

import java.util.UUID;

public class RoleFactory {
    public Role roleUser() {
        return new Role(UUID.randomUUID(), RoleName.ROLE_USER);
    }

    public Role roleAdmin() {
        return new Role(UUID.randomUUID(), RoleName.ROLE_ADMIN);
    }
}
