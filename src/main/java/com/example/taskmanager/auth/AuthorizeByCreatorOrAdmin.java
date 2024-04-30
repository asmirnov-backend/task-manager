package com.example.taskmanager.auth;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("returnObject.creator.id == authentication.principal || hasRole('ADMIN')")
public @interface AuthorizeByCreatorOrAdmin {
}
