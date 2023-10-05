package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.LoginDTO;
import com.fairgoods.webshop.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class AuthenticationController {


        private final AuthenticationService authenticationService;

        @PostMapping("/login")
        public String login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
            return "Bearer " + authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword());
        }
    }

