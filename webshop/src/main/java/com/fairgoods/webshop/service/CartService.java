package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.Cart;
import com.fairgoods.webshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}