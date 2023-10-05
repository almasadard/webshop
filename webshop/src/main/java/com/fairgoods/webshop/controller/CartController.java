/*
package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.model.Cart;
import com.fairgoods.webshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ResponseStatus(code = CREATED)
    @PostMapping
    public Cart create(@RequestBody Cart cart) {
        return cartService.save(cart);
    }
}*/
