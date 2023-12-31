package com.fairgoods.webshop.controllerTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Autowired
    private ObjectMapper objectMapper;

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

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void getUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user").header("Authorization", token))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.length()", Matchers.is(3)))
                .andExpect(jsonPath("$.[*]").isNotEmpty());

        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByIdTest() throws Exception {

        // Find User und filtere den, den wir durch E-Mail wollen, und hole die ID
        final User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("user1@test.com"))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        final Long userId = user.getId();

        // Test mit gültiger Benutzer-ID und Email und Autorisierungstoken
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Prüft ob ID im JSON existiert und ob sie mit ID der Datenbank übereinstimmt
                .andExpect(jsonPath("$.id", Matchers.is(userId.intValue())))
                .andExpect(jsonPath("$.email", Matchers.is(user.getEmail())));

        // Test mit gültiger Benutzer-ID aber ohne Autorisierungstoken
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstname("Test");
        userDTO.setLastname("User");
        userDTO.setEmail("testuser@test.com");
        userDTO.setPassword("password");

        String jsonRequest = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user") // Stellen Sie sicher, dass der Pfad korrekt ist
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", Matchers.is(userDTO.getFirstname())))
                .andExpect(jsonPath("$.lastname", Matchers.is(userDTO.getLastname())))
                .andExpect(jsonPath("$.email", Matchers.is(userDTO.getEmail())));
    }

    @Test
    void updateUserTest() throws Exception {
        // Finden des zu aktualisierenden Benutzers
        User userToUpdate = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("user1@test.com"))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Erstellen des UserDTO mit aktualisierten Daten
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userToUpdate.getId());
        updatedUserDTO.setFirstname("UpdatedName");
        updatedUserDTO.setLastname("UpdatedLastName");
        updatedUserDTO.setEmail("updatedemail@test.com");
        updatedUserDTO.setPassword("updatedPassword");

        String jsonRequest = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(updatedUserDTO.getId().intValue())))
                .andExpect(jsonPath("$.firstname", Matchers.is(updatedUserDTO.getFirstname())))
                .andExpect(jsonPath("$.lastname", Matchers.is(updatedUserDTO.getLastname())))
                .andExpect(jsonPath("$.email", Matchers.is(updatedUserDTO.getEmail())));

    }


    @Test
    void deleteUserTest() throws Exception {
        final User user = userRepository.findAll().stream()
                //.filter(p -> p.getId().equals(2L))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        final Long userId = user.getId();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/{id}", userId)
                        .header("Authorization", token))
                .andExpect(status().is2xxSuccessful());

    }
}