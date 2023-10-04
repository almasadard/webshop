package com.fairgoods.webshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fairgoods.webshop.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.security.Keys;


@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties properties;

    private static final int EXPIRES_IN = 3600000;
    private static final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String issue(Request request){
        return JWT.create()
                .withSubject(String.valueOf(request.getUserId()))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("email", request.getEmail())
                .withClaim("auth", request.getRoles())
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    public String generateToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRES_IN);
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getEncoded());

        return Jwts.builder()
                .claim("id", user.getId())
                .claim("sub", user.getEmail())
                .claim("admin", user.isAdmin())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    @Getter
    @Builder
    public static class Request {
        private final Long userId;
        private final String email;
        private final List<String> roles;
    }

}
