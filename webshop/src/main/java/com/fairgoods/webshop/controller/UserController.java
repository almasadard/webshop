package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDTO> getUsers(){
        return service.findAll().stream()
                .map(service::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable Long id) {
        return service.toDTO(service.findById(id));
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return service.save(userDTO);
    }

    @PutMapping
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
        return service.update(userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        service.deleteById(id);
    }

}
