package com.fairgoods.webshop.service;


import com.fairgoods.webshop.dto.LoginRequest;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.security.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final UserRepository userRepository;

    public String attemptLogin(LoginRequest request) {
        var user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());

        if (user.isEmpty()) throw new RuntimeException();

        return jwtIssuer.generateToken(user.get());

    }
}