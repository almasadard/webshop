package com.fairgoods.webshop.service;
import com.fairgoods.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public String login(String email, String password) {
        var user = userRepository.findByEmailAndPassword(email, password);

        if (user.isEmpty()) {
            throw new BadRequestException("Benutzer existiert nicht");
        }

        return tokenService.generateToken(user.get());
    }
}
