package com.fairgoods.webshop.service;


import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.security.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final UserRepository userRepository;

    public String attemptLogin(String email, String password) {
        var user = userRepository.findByEmailAndPassword(email, password);

        if (user.isEmpty()) throw new RuntimeException();

        return jwtIssuer.generateToken(user.get());

    }
}