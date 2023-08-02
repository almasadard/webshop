package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> getUsers(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findUserById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return service.save(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return service.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        service.deleteById(id);
    }

}
