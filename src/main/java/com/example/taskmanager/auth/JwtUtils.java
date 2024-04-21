package com.example.taskmanager.auth;

import com.example.taskmanager.user.RoleName;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoleNames(getRoles(claims));
        jwtInfoToken.setEmail(claims.get("email", String.class));
        return jwtInfoToken;
    }

    private static Set<RoleName> getRoles(Claims claims) {
        final List<String> scope = claims.get("scope", List.class);

        return scope.stream()
                .map(RoleName::valueOf)
                .collect(Collectors.toSet());
    }
}
