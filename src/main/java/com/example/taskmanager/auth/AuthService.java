package com.example.taskmanager.auth;

import com.example.taskmanager.auth.dto.LoginDto;
import com.example.taskmanager.auth.dto.RegistrationDto;
import com.example.taskmanager.auth.dto.TokensDto;
import com.example.taskmanager.auth.exception.IncorrectCredentialsException;
import com.example.taskmanager.auth.exception.InvalidRefreshTokenException;
import com.example.taskmanager.user.User;
import com.example.taskmanager.user.UserService;
import com.example.taskmanager.user.exception.UserAlreadyExistException;
import com.example.taskmanager.user.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokensDto login(LoginDto loginDto) throws IncorrectCredentialsException {
        final User user = userService
                .findByEmail(loginDto.getEmail())
                .orElseThrow(IncorrectCredentialsException::new);

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokensDto(accessToken, refreshToken);
    }

    public TokensDto registration(RegistrationDto registrationDto) throws UserAlreadyExistException {
        User user = userService.create(registrationDto);

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokensDto(accessToken, refreshToken);
    }

    public TokensDto createAccessToken(String refreshToken) throws UserNotFoundException, InvalidRefreshTokenException {
        throwIfRefreshTokenIsInvalid(refreshToken);

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final UUID userId = UUID.fromString(claims.get("id", String.class));

        final User user = userService.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        final String accessToken = jwtProvider.generateAccessToken(user);
        return new TokensDto(accessToken, null);
    }

    public TokensDto refresh(String refreshToken) throws UserNotFoundException, InvalidRefreshTokenException {
        throwIfRefreshTokenIsInvalid(refreshToken);

        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final UUID userId = UUID.fromString(claims.get("id", String.class));

        final User user = userService.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new TokensDto(accessToken, newRefreshToken);
    }

    private void throwIfRefreshTokenIsInvalid(String refreshToken) throws InvalidRefreshTokenException {
        if (!jwtProvider.isValidRefreshToken(refreshToken)) throw new InvalidRefreshTokenException();
    }
}
