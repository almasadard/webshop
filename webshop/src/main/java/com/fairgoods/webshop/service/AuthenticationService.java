package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public String login(String email, String password, boolean requireAdmin) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Benutzer existiert nicht");
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(password, user.getPassword())) {
            return tokenService.generateToken(user);
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Falsches Passwort";
        }

        // Überprüfe, ob der Benutzer ein Admin ist, wenn requireAdmin true ist
        if (requireAdmin && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not an admin");
        }
        return "Login fehlgeschlagen - Schau im AuthService";
    }
}
