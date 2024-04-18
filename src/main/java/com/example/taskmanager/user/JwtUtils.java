package com.example.taskmanager.user;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Enum.valueOf;

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
        final List<LinkedHashMap> roles = claims.get("roles", List.class);
        log.info(String.valueOf(roles));

        return roles.stream()
                .map(role -> RoleName.valueOf((String) role.get("name")))
                .collect(Collectors.toSet());
    }
}
