package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User save (User user){
        return userRepository.save(user);
    }

    public User update(User updateUser){
        User user = userRepository.findById(updateUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateUser.getId()));

        user.setTitle(updateUser.getTitle());
        user.setFirstname(updateUser.getFirstname());
        user.setLastname(updateUser.getLastname());
        user.setStreetname(updateUser.getStreetname());
        user.setPostcode(updateUser.getPostcode());
        user.setEmail(updateUser.getEmail());
        user.setPassword(updateUser.getPassword());

        return userRepository.save(user);

    }

    public void deleteById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);

    }
}
