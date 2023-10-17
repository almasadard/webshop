package com.fairgoods.webshop.controllerTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.TokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MockMvc mockMvc;

    private String token = "";

    @BeforeEach
    void setup() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstname("User1");
        user1.setLastname("UserL1");
        user1.setEmail("user1@test.com");
        user1.setPassword("*geheim1");
        user1.setAdmin(true);

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
        token = "Bearer " + tokenService.generateToken(user1);
    }

    @Test
    public void getUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(3)))
                .andExpect(jsonPath("$.[*]").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        // Test mit gültiger Benutzer-ID und Email und Autorisierungstoken
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1").header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@test.com"));

        // Test mit gültiger Benutzer-ID aber ohne Autorisierungstoken
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isForbidden());
    }
}