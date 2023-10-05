package com.fairgoods.webshop.service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
public class TokenService {

    @Value("${spring.security.jwt.secretkey}")
    private String secretkey;
    private static final int EXPIRES_IN = 3600000;


    public String generateToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRES_IN);
        Key key = Keys.hmacShaKeyFor(secretkey.getBytes());

        return Jwts.builder()
                .claim("id", user.getId())
                .claim("sub", user.getEmail())
                .claim("admin", user.isAdmin())
                .setExpiration(expirationDate)
                .signWith(key, HS256)
                .compact();
    }

    public Optional<UserPrincipal> parseToken(String jwt) {
        Jws<Claims> jwsClaims;

        try {
            jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(secretkey.getBytes())
                    .build()
                    .parseClaimsJws(jwt);
        } catch (SignatureException e) {
            return Optional.empty();
        }

        Long userId = jwsClaims.getBody().get("id", Long.class);
        String sub = jwsClaims.getBody().getSubject();
        boolean admin = jwsClaims.getBody().get("admin", Boolean.class);

        return Optional.of(new UserPrincipal(userId, sub, admin));
    }
}