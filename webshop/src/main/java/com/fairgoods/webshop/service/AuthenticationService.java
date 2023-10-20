package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user with given email and password, and optionally checks for admin role.
     *
     *
     * @param email         The email of the user attempting to login.
     * @param password      The password provided by the user attempting to login.
     * @param requireAdmin  A boolean flag indicating whether to check for admin role or not.
     * @return A string representing the generated JWT token, or an error message.
     * @throws ResponseStatusException if the email doesn’t exist or incorrect password is provided
     * or if the admin role is required but the user is not an admin.
     */
    public String login(String email, String password, boolean requireAdmin) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist.");
        }

        User user = userOptional.get();

        if (passwordEncoder.matches(password, user.getPassword())) {
            return tokenService.generateToken(user);
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Wrong password";
        }

        // Überprüfe, ob der Benutzer ein Admin ist, wenn requireAdmin true ist
        if (requireAdmin && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not an admin");
        }
        return "Login failed - Look at AuthService";
    }
}
