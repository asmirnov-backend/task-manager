package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDTO;
import com.example.taskmanager.auth.dto.RefreshJwtRequestDTO;
import com.example.taskmanager.auth.dto.TokensDTO;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokensDTO login(@RequestBody LoginDTO loginDTO) throws AuthException {
        return authService.login(loginDTO);
    }

    @PostMapping("token")
    public TokensDTO getNewAccessToken(@RequestBody RefreshJwtRequestDTO request) throws AuthException {
        return authService.getAccessToken(request.getRefreshToken());
    }

    @PostMapping("refresh")
    public TokensDTO getNewRefreshToken(@RequestBody RefreshJwtRequestDTO request) throws AuthException {
        return authService.refresh(request.getRefreshToken());
    }

}
