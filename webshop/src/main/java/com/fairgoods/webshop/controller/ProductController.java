package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing products.
 * Supports CRUD operations on products.
 *
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Retrieve all products.
     *
     * @return A list of all products.
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    /**
     * Retrieve a product by its ID.
     *
     * @param id The ID of the product.
     * @return The found product, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * Create a new product.
     *
     * @param product The product to be saved.
     * @return A response with status 201 (Created) if the product was successfully created, else a 500 (Internal Server Error) response.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        try {
            productService.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred creating product");
        }
    }

    /**
     * Update an existing product.
     *
     * @param productDTO The updated product.
     * @return The updated product, or a 404 status if not found.
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(productDTO));
    }


    /**
     * Delete a product by its ID.
     *
     * @param id The ID of the product to be deleted.
     * @return A response with status 204 (No Content) if the product was successfully deleted.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}