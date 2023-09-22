package com.fairgoods.webshop.service;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Cart;
import com.fairgoods.webshop.model.Position;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.PositionRepository;
import com.fairgoods.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;

    public Optional<Position> findById(Long id) {
        return positionRepository.findById(id);
    }

    public Position save(Position position, Long userId, Long productId) {
        Cart cart = cartService.findByUserId(userId);

        if (cart == null) {
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                cart = cartService.save(new Cart(user.get()));
            } else {
                throw new RuntimeException("User does not exist");
            }
        }

        Optional<ProductDTO> product = productService.findById(productId);

        if (product.isEmpty()) {
            throw new RuntimeException("Product does not exist");
        }

        position.setCart(cart);
        position.setProduct(productService.toEntity(product.get()));

        return positionRepository.save(position);
    }
}