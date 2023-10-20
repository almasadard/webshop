package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.security.UserPrincipal;
import com.fairgoods.webshop.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstname("Test");
        testUser.setLastname("User");
        testUser.setEmail("test@test.com");
        testUser.setAdmin(true);

    }

    @Test
    void generateTokenTest() {
        String token = assertDoesNotThrow(() -> tokenService.generateToken(testUser));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void parseTokenTest() {
        String token = assertDoesNotThrow(() -> tokenService.generateToken(testUser));

        // Erfolgreiches Parsen
        Optional<UserPrincipal> userPrincipalOptional = assertDoesNotThrow(() -> tokenService.parseToken(token));
        assertTrue(userPrincipalOptional.isPresent());


        // Test mit ungültigem Token
        Optional<UserPrincipal> invalidUserPrincipalOptional = assertDoesNotThrow(() -> tokenService.parseToken("blablabla"));
        assertFalse(invalidUserPrincipalOptional.isPresent());
    }
}
