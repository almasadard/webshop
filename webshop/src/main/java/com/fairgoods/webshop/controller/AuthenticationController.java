package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.LoginDTO;
import com.fairgoods.webshop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for logging in a user.
     *
     * @param loginDTO DTO containing login credentials.
     * @return ResponseEntity with the Bearer token if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword(), false);
        return ResponseEntity.ok(addBearerPrefix(token));
    }

    /**
     * Endpoint to check if the authenticated user has admin role.
     *
     * @return true if the authenticated user is an admin, otherwise not accessible.
     */
    @GetMapping("/isAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean isAdmin() {
        return true;
    }

    /**
     * Helper method to add "Bearer" prefix to the token if it's not already prefixed.
     *
     * @param token the token string.
     * @return token string with "Bearer" prefix.
     */
    private String addBearerPrefix(String token) {
        String pre = "Bearer ";
        if (!token.startsWith(pre)) {
            return pre.concat(token);
        }
        return token;
    }
}
