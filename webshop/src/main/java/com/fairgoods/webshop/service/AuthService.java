package com.fairgoods.webshop.service;

import com.fairgoods.webshop.dto.LoginResponse;
import com.fairgoods.webshop.security.JwtIssuer;
import com.fairgoods.webshop.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var tokenRequest = JwtIssuer.Request.builder()
                .userId(principal.getUserId())
                .email(principal.getEmail())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        var token = jwtIssuer.issue(tokenRequest);

        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }
}


/*
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String email, String password) {
        System.out.println("Attempting login. . .");
        try {

            var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        } catch (Exception e) {
            throw new RuntimeException("NOPE this is a " + e.toString());
        }
        /*System.out.println("Authorities: " + authentication.getAuthorities()
                + " / is authorized: " + authentication.isAuthenticated()
                + " / name: " + authentication.getName()
                + " / credentials: " + authentication.getCredentials()
                + " / details: " + authentication.getDetails()
                + " / principle: " + authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var principal = (UserPrincipal) authentication.getPrincipal();

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
*/

/*
        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }
}

*/