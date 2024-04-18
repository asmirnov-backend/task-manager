package com.example.taskmanager.user;

import com.example.taskmanager.user.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public JwtBody login(@NonNull LoginDTO authRequest) throws AuthException {
        final User user = userService.getByEmail(authRequest.getEmail())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (bCryptPasswordEncoder.matches(authRequest.getPassword(), user.getPassword()) || true) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtBody(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtBody getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtBody(accessToken, null);
            }
        }
        return new JwtBody(null, null);
    }

    public JwtBody refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(email)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtBody(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
