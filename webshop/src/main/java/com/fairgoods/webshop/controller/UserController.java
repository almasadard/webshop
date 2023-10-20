package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing users.
 * Supports CRUD operations on users.
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    /**
     * Retrieve all users.
     *
     * @return A list of all users.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getUsers(){
        return service.findAll().stream()
                .map(service::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a user by its ID.
     *
     * @param id The ID of the user.
     * @return The found user, or a 404 status if not found.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable Long id) {
        return service.toDTO(service.findById(id));
    }

    /**
     * Create a new user.
     *
     * @param userDTO The user data transfer object containing user details.
     * @return The created user.
     */
    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return service.save(userDTO);
    }

    /**
     * Update an existing user.
     *
     * @param userDTO The user data transfer object containing updated user details.
     * @return The updated user.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
        return service.update(userDTO);
    }

    /**
     * Delete a user by its ID.
     *
     * @param id The ID of the user to be deleted.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        service.deleteById(id);
    }

}
