package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDTO;
import com.example.taskmanager.auth.dto.RefreshJwtRequestDTO;
import com.example.taskmanager.auth.dto.RegistrationDTO;
import com.example.taskmanager.auth.dto.TokensDTO;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokensDTO login(@Valid @RequestBody LoginDTO loginDTO) throws AuthException {
        return authService.login(loginDTO);
    }

    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public TokensDTO registration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        return authService.registration(registrationDTO);
    }

    @PostMapping("token")
    @PreAuthorize("hasRole('USER')")
    public TokensDTO getNewAccessToken(@Valid @RequestBody RefreshJwtRequestDTO request) throws AuthException {
        return authService.createAccessToken(request.getRefreshToken());
    }

    @PostMapping("refresh")
    @PreAuthorize("hasRole('USER')")
    public TokensDTO getNewRefreshToken(@Valid @RequestBody RefreshJwtRequestDTO request) throws AuthException {
        return authService.refresh(request.getRefreshToken());
    }
}
