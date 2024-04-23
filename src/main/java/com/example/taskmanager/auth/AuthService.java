package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDTO;
import com.example.taskmanager.auth.dto.TokensDTO;
import com.example.taskmanager.user.User;
import com.example.taskmanager.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokensDTO login(LoginDTO loginDTO) throws AuthException {
        final User user = userService.findByEmail(loginDTO.getEmail())
                .orElseThrow(IncorrectCredentialsException::new);

        if (passwordEncoder.matches(user.getPassword(), loginDTO.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokensDTO(accessToken, refreshToken);
    }

    public TokensDTO getAccessToken(String refreshToken) throws AuthException {
        throwIfRefreshTokenIsInvalid(refreshToken);

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String email = claims.getSubject();

        final User user = userService.findByEmail(email)
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        final String accessToken = jwtProvider.generateAccessToken(user);
        return new TokensDTO(accessToken, null);
    }

    public TokensDTO refresh(String refreshToken) throws AuthException {
        throwIfRefreshTokenIsInvalid(refreshToken);

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String email = claims.getSubject();

        final User user = userService.findByEmail(email)
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new TokensDTO(accessToken, newRefreshToken);
    }

    private void throwIfRefreshTokenIsInvalid(String refreshToken) {
        if (!jwtProvider.isValidRefreshToken(refreshToken)) throw new InvalidRefreshTokenException();
    }
}
