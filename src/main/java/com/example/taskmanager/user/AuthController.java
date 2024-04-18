package com.example.taskmanager.user;

import com.example.taskmanager.user.dto.LoginDTO;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JwtBody> login(@RequestBody LoginDTO authRequest) throws AuthException {
        final JwtBody token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtBody> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtBody token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtBody> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtBody token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
