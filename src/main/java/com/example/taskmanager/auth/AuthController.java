package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDto;
import com.example.taskmanager.auth.dto.RefreshJwtRequestDto;
import com.example.taskmanager.auth.dto.RegistrationDto;
import com.example.taskmanager.auth.dto.TokensDto;
import com.example.taskmanager.user.UserAlreadyExistException;
import com.example.taskmanager.user.UserNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokensDto login(@Valid @RequestBody LoginDto loginDTO) throws AuthException {
        return authService.login(loginDTO);
    }

    @PostMapping("registration")
    @ResponseStatus(HttpStatus.CREATED)
    public TokensDto registration(@Valid @RequestBody RegistrationDto registrationDTO) throws UserAlreadyExistException {
        return authService.registration(registrationDTO);
    }

    @PostMapping("token")
    @PreAuthorize("hasRole('USER')")
    public TokensDto getNewAccessToken(@Valid @RequestBody RefreshJwtRequestDto request) throws UserNotFoundException, InvalidRefreshTokenException {
        return authService.createAccessToken(request.getRefreshToken());
    }

    @PostMapping("refresh")
    @PreAuthorize("hasRole('USER')")
    public TokensDto getNewRefreshToken(@Valid @RequestBody RefreshJwtRequestDto request) throws UserNotFoundException, InvalidRefreshTokenException {
        return authService.refresh(request.getRefreshToken());
    }
}
