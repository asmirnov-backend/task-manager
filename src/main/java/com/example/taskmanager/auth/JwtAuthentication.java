package com.example.taskmanager.auth;

import com.example.taskmanager.user.RoleName;
import com.example.taskmanager.user.User;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private UUID id;
    private String username;
    private String email;
    private Set<RoleName> roleNames;
    private User userReference;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleNames;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
