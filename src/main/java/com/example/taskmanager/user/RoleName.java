package com.example.taskmanager.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleName implements GrantedAuthority {
    ADMIN("ADMIN");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
