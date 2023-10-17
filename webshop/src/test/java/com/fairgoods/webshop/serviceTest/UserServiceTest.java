package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstname("User1");
        user1.setLastname("UserL1");
        user1.setEmail("user1@test.com");
        user1.setPassword("*geheim1");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstname("User2");
        user2.setLastname("UserL2");
        user2.setEmail("user2@test.com");
        user2.setPassword("*geheim1");

        User user3 = new User();
        user3.setId(3L);
        user3.setFirstname("User3");
        user3.setLastname("UserL3");
        user3.setEmail("user3@test.com");
        user3.setPassword("*geheim1");

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void findAllUserTest() {
        List<User> users = assertDoesNotThrow(() -> userService.findAll());
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void findByEmailTest() {
        Optional<User> userOptional = assertDoesNotThrow(() -> userService.findByEmail("user1@test.com"));
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        assertNotNull(user);
        assertEquals("user1@test.com", user.getEmail());
    }
}