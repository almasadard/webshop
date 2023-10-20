package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@Import(BCryptPasswordEncoder.class)
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


    @Test
    void saveTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(4L);
        userDTO.setFirstname("User4");
        userDTO.setLastname("UserL4");
        userDTO.setEmail("user4@test.com");
        userDTO.setPassword("");

        // Speichern des Benutzers und Überprüfung, ob keine Ausnahme ausgelöst wird
        UserDTO savedUserDTO = assertDoesNotThrow(() -> userService.save(userDTO));
        assertNotNull(savedUserDTO);

        // Überprüfen des gespeicherten Benutzers auf Konsistenz
        assertNotNull(savedUserDTO.getId());
        assertEquals("User4", savedUserDTO.getFirstname());
        assertEquals("UserL4", savedUserDTO.getLastname());
        assertEquals("user4@test.com", savedUserDTO.getEmail());

        // Konvertieren des DTOs zur Entität und Überprüfen, ob keine Ausnahme ausgelöst wird
        User savedUser = assertDoesNotThrow(() -> userService.toEntity(savedUserDTO));

        // Überprüfen der gespeicherten Entität auf Konsistenz
        assertNotNull(savedUser);
        assertEquals("User4", savedUser.getFirstname());
        assertEquals("UserL4", savedUser.getLastname());
        assertEquals("user4@test.com", savedUser.getEmail());
    }

    @Test
    void updateTest() {
        // Benutzer erstellen
        User userToUpdate = new User();
        userToUpdate.setFirstname("Agata");
        userToUpdate.setLastname("Christie");
        userToUpdate.setEmail("agata@test.com");
        userToUpdate.setPassword("*toupdate");

        // Benutzer speichern
        User savedUser = userRepository.save(userToUpdate);

        // Daten zum Aktualisieren vorbereiten
        UserDTO updateUserDTO = new UserDTO();
        updateUserDTO.setId(savedUser.getId());
        updateUserDTO.setFirstname("Agatha");
        updateUserDTO.setLastname("Christie");
        updateUserDTO.setEmail("agatha@test.com");
        updateUserDTO.setPassword("*updated");

        // Benutzer aktualisieren
        UserDTO updatedUserDTO = userService.update(updateUserDTO);

        // Überprüfen, ob die Aktualisierung erfolgreich war
        assertNotNull(updatedUserDTO);
        assertEquals("Agatha", updatedUserDTO.getFirstname());
        assertEquals("Christie", updatedUserDTO.getLastname());
        assertEquals("agatha@test.com", updatedUserDTO.getEmail());

        // Überprüfen, ob die Aktualisierung in der Datenbank gespeichert wurde
        Optional<User> userOptional = userRepository.findById(savedUser.getId());
        assertTrue(userOptional.isPresent());

        User updatedUser = userOptional.get();
        assertEquals("Agatha", updatedUser.getFirstname());
        assertEquals("Christie", updatedUser.getLastname());
        assertEquals("agatha@test.com", updatedUser.getEmail());
    }


    @Test
    void deleteByIdTest() {
        User userToDelete = new User();
        userToDelete.setFirstname("Elisabeth");
        userToDelete.setLastname("Queen");
        userToDelete.setEmail("lizzie@test.com");
        userToDelete.setPassword("*todelete");

        User savedUser = userRepository.save(userToDelete);

        // Überprüfen, ob der Benutzer vor dem Löschen existiert
        Optional<User> userOptionalBeforeDelete = userRepository.findById(savedUser.getId());
        assertTrue(userOptionalBeforeDelete.isPresent());

        // Löschen des Benutzers und Überprüfen, ob keine Ausnahme ausgelöst wird
        assertDoesNotThrow(() -> userService.deleteById(savedUser.getId()));

        // Überprüfen, ob der Benutzer nach dem Löschen nicht mehr existiert
        Optional<User> userOptionalAfterDelete = userRepository.findById(savedUser.getId());
        assertFalse(userOptionalAfterDelete.isPresent());
    }

}