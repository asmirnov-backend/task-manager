package com.example.taskmanager.user;

import java.util.UUID;

public class RoleFactory {
    public Role role_user() {
        return new Role(UUID.randomUUID(), RoleName.ROLE_USER);
    }

    public Role role_admin() {
        return new Role(UUID.randomUUID(), RoleName.ROLE_ADMIN);
    }
}
