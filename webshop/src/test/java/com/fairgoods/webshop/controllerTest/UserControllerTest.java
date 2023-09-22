package com.fairgoods.webshop.controllerTest;

import com.fairgoods.webshop.controller.UserController;
import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testFindUserById() throws Exception {
        // Erstellen Sie ein Mock-UserDTO für den Test
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setFirstname("John");

        // Mocken des Service, um die findById-Methode zu überschreiben
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));

        // Durchführung des MockMvc-Tests
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"));
    }
}
