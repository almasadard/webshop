package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllUsers() {
        // Mocking des UserRepository, um die findAll-Methode zu überschreiben
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "John"));
        userList.add(new User(2L, "Alice"));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindUserById() {
        // Mocking des UserRepository, um die findById-Methode zu überschreiben
        User user = new User(1L, "John");
        UserDTO userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setFirstname("John");
        userService.save(userDto);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals("John", result.getFirstname());
    }

    @Test
    public void testFindUserByIdNotFound() {
        // Mocking des UserRepository, um die findById-Methode zu überschreiben
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
    }


}
