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

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The user if found.
     * @throws ObjectNotFoundException if the user is not found.
     */
    public User findById(Long id){
        var findUserById = userRepository.findById(id);
        if(findUserById.isEmpty()){
            throw new ObjectNotFoundException(findUserById, "User nicht gefunden");
        }
        return findUserById.get();
    }

    /**
     * Saves a new user or updates an existing one.
     *
     * @param userDTO The user details.
     * @return The saved or updated user.
     */
    public UserDTO save (UserDTO userDTO){
        User user = toEntity(userDTO);
        user = userRepository.save(user);
        return toDTO(user);
    }

    /**
     * Updates the details of an existing user.
     *
     * @param updateUserDTO The new user details.
     * @return The updated user details.
     * @throws EntityNotFoundException if the user is not found.
     */
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

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws EntityNotFoundException if the user is not found.
     */
    public void deleteById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDTO.
     */
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
        userDTO.setPassword("");
        userDTO.setAdmin(user.isAdmin());
        return userDTO;
    }


    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDTO The UserDTO to convert.
     * @return The corresponding User entity.
     */
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
