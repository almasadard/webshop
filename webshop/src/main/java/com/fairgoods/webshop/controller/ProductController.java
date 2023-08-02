package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CRUD

    // Read Product(s)

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create Product

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        return ResponseEntity.ok(productService.save(product));
    }

    // Update Product

    @PutMapping
    public ResponseEntity<Product> updateProduct (@Valid @RequestBody Product product){
        return ResponseEntity.ok(productService.update(product));
    }

    // Delete Product

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}