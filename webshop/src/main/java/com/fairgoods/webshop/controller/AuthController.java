package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.LoginRequest;
import com.fairgoods.webshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/user")
    public String login(@RequestBody LoginRequest request) {
        System.out.println("email: " + request.getEmail() + " pw: " + request.getPassword());

        return "Bearer " + authService.attemptLogin(request);

    }
}
