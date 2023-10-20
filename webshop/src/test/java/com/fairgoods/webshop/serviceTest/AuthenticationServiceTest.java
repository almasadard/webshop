package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.service.AuthenticationService;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setFirstname("Test");
        user.setLastname("User");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAdmin(true);
        userRepository.save(user);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void loginTest() {
        // Erfolgreicher Login-Test
        assertDoesNotThrow(() -> authenticationService.login("test@test.com", "password", false));

        // Passwort falsch
        String response = authenticationService.login("test@test.com", "wrongpassword", false);
        assertEquals("Wrong password", response);

    }
}
