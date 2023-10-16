package com.fairgoods.webshop.service;

import com.fairgoods.webshop.dto.UserDTO;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id){
        var findUserById = userRepository.findById(id);
        if(findUserById.isEmpty()){
            throw new ObjectNotFoundException(findUserById, "User nicht gefunden");
        }
        return findUserById.get();
    }

    public UserDTO save (UserDTO userDTO){
        User user = toEntity(userDTO);
        user = userRepository.save(user);
        return toDTO(user);
    }

    public UserDTO update(UserDTO updateUserDTO){
        User user = userRepository.findById(updateUserDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateUserDTO.getId()));

        user.setTitle(updateUserDTO.getTitle());
        user.setFirstname(updateUserDTO.getFirstname());
        user.setLastname(updateUserDTO.getLastname());
        user.setStreetname(updateUserDTO.getStreetname());
        user.setPostcode(updateUserDTO.getPostcode());
        user.setEmail(updateUserDTO.getEmail());
        user.setPassword(updateUserDTO.getPassword());

        return toDTO(userRepository.save(user));

    }

    public void deleteById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);

    }



    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setTitle(user.getTitle());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setStreetname(user.getStreetname());
        userDTO.setPostcode(user.getPostcode());
        userDTO.setCity(user.getCity());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(null);
        userDTO.setAdmin(user.isAdmin());
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setTitle(userDTO.getTitle());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setStreetname(userDTO.getStreetname());
        user.setPostcode(userDTO.getPostcode());
        user.setCity(userDTO.getCity());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAdmin(userDTO.isAdmin());
        return user;
    }
}
