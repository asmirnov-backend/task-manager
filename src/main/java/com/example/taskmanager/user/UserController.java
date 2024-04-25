package com.example.taskmanager.user;

import com.example.taskmanager.auth.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    User getCurrentUser(JwtAuthentication authentication) throws UserNotFoundException {
        return userService.findByIdOrThrow(authentication.getId());
    }
}
