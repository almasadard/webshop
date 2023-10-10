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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword(), false);
        return ResponseEntity.ok(addBearerPrefix(token));
    }

/*    @PostMapping("/login/admin")
    public ResponseEntity<String> adminLogin(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword(), true);
        return ResponseEntity.ok(addBearerPrefix(token));
    }*/

    @GetMapping("/isAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean isAdmin() {
        return true;
    }

    private String addBearerPrefix(String token) {
        String pre = "Bearer ";
        if (!token.startsWith(pre)) {
            return pre.concat(token);
        }
        return token;
    }
}
