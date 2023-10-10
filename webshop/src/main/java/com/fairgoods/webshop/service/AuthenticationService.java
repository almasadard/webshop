package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public String login(String email, String password, boolean requireAdmin) {
        var userOptional = userRepository.findByEmailAndPassword(email, password);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Benutzer existiert nicht");
        }

        User user = userOptional.get();

        // Überprüfe, ob der Benutzer ein Admin ist, wenn requireAdmin true ist
        if (requireAdmin && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not an admin");
        }

        return tokenService.generateToken(user);
    }
}
