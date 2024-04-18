package com.example.taskmanager.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Date created_at;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updated_at;

    @Column(length = 127, nullable = false, unique = true)
    private String email;

    @Column(length = 63, nullable = false, unique = true)
    private String username;

    @Column(length = 127, nullable = false)
    private String name;

    @Column(length = 127, nullable = false)
    private String surname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
