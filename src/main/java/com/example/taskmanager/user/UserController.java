package com.example.taskmanager.user;

import com.example.taskmanager.auth.JwtAuthentication;
import com.example.taskmanager.user.dto.UpdateUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    User updateCurrentUser(JwtAuthentication authentication, @Valid @RequestBody UpdateUserDTO updateUserDTO) throws UserNotFoundException {
        return userService.updateUser(authentication.getId(), updateUserDTO);
    }
}
