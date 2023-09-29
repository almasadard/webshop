package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.LoginRequest;
import com.fairgoods.webshop.dto.LoginResponse;
import com.fairgoods.webshop.security.JwtIssuer;
import com.fairgoods.webshop.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/user")
    public LoginResponse login(@RequestBody LoginRequest request) {
        System.out.println("email: " + request.getEmail() + " pw: " + request.getPassword());

        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var principal = (UserPrincipal) authentication.getPrincipal();
            var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            var tokenRequest = JwtIssuer.Request.builder()
                    .userId(principal.getUserId())
                    .email(principal.getEmail())
                    .roles(roles)
                    .build();
            var token = jwtIssuer.issue(tokenRequest);

            return LoginResponse.builder()
                    .accessToken(token)
                    .build();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return null;
    }
}