package com.fairgoods.webshop.controllerTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.controller.AuthenticationController;
import com.fairgoods.webshop.dto.LoginDTO;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private String token;
    @Autowired
    AuthenticationController authenticationController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;



    @BeforeEach
    void setup() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstname("User1");
        user1.setLastname("UserL1");
        user1.setEmail("user1@test.com");
        user1.setPassword("$2a$10$sTBVydM46IkLmih/qu.X/eqbvcvGHUXZtoioKfiuMZ5XCXT4laVUa");
        user1.setAdmin(false);

        User admin = new User();
        admin.setId(2L);
        admin.setFirstname("Admin");
        admin.setLastname("Adminus");
        admin.setEmail("admin@test.com");
        admin.setPassword("$2a$10$sTBVydM46IkLmih/qu.X/eqbvcvGHUXZtoioKfiuMZ5XCXT4laVUa");
        admin.setAdmin(true);


        userRepository.saveAll(List.of(user1, admin));
        token = "Bearer " + tokenService.generateToken(admin);
    }

    @Test
    void loginTest() throws Exception {
        final String email = "user1@test.com";
        final String password = "*geheim1";
        final LoginDTO credentials = new LoginDTO(email, password);

        // Abgleich von email,password von user1 aus setup

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(mapper.writeValueAsString(credentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void isAdminTest() throws Exception {

        final String email = "admin@test.com";
        final String password = "*geheim1";
        final LoginDTO credentials = new LoginDTO(email, password);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/isAdmin")
                        .content(mapper.writeValueAsString(credentials))
                        .header("Authorization", token)) // Setzt den Authorization-Header mit dem Bearer-Token
                .andExpect(status().isOk());
    }
}