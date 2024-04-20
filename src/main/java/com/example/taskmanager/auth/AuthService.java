package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.TokensDTO;
import com.example.taskmanager.user.*;
import com.example.taskmanager.auth.dto.LoginDTO;
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

    public TokensDTO login(LoginDTO loginDTO) throws AuthException {
        final User user = userService.findByEmail(loginDTO.getEmail())
                .orElseThrow(IncorrectCredentialsException::new);

        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), refreshToken);

        return new TokensDTO(accessToken, refreshToken);
    }

    public TokensDTO getAccessToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) throw new InvalidRefreshTokenException();

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String email = claims.getSubject();
        final String savedRefreshToken = refreshStorage.get(email);

        if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
            final User user = userService.findByEmail(email)
                    .orElseThrow(() -> new AuthException("Пользователь не найден"));
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new TokensDTO(accessToken, null);
        } else {
            throw new InvalidRefreshTokenException();
        }
    }

    public TokensDTO refresh(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) throw new InvalidRefreshTokenException();


        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String email = claims.getSubject();
        final String saveRefreshToken = refreshStorage.get(email);

        if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
            final User user = userService.findByEmail(email)
                    .orElseThrow(() -> new AuthException("Пользователь не найден"));
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String newRefreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), newRefreshToken);
            return new TokensDTO(accessToken, newRefreshToken);
        } else {
            throw new InvalidRefreshTokenException();
        }
    }
}
