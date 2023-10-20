package com.fairgoods.webshop.service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.security.UserPrincipal;
import io.jsonwebtoken.*;
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

    /**
     * Generates a JWT token for a user. The token contains claims like user ID, email, and admin status.
     * The token is signed with HMAC using SHA-256 hash algorithm.
     *
     * @param user The user for whom the token is generated.
     * @return A JWT token string.
     */
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

    /**
     * Parses a JWT token to extract the user principal information such as user ID, email, and admin status.
     * It verifies the token signature and checks for token expiration and format.
     *
     * @param jwt The JWT token string to parse.
     * @return An Optional containing a UserPrincipal if the token is valid, or an empty Optional if the token is invalid or expired.
     */
    public Optional<UserPrincipal> parseToken(String jwt) {
        Jws<Claims> jwsClaims;

        try {
            jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(secretkey.getBytes())
                    .build()
                    .parseClaimsJws(jwt);

            Long userId = jwsClaims.getBody().get("id", Long.class);
            String sub = jwsClaims.getBody().get("sub", String.class);
            boolean admin = jwsClaims.getBody().get("admin", Boolean.class);

            return Optional.of(new UserPrincipal(userId, sub, admin));

        } catch (SignatureException e) {
            // Ungültige Signatur
            return Optional.empty();
        } catch (ExpiredJwtException e) {
            // Token ist abgelaufen
            return Optional.empty();
        } catch (MalformedJwtException e) {
            // Token-Format ist nicht korrekt
            return Optional.empty();
        } catch (UnsupportedJwtException e) {
            // Token hat eine unbekannte Struktur
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            // Claims sind leer oder illegal
            return Optional.empty();
        }
    }
}