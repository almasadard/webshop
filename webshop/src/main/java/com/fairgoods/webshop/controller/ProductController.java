package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CRUD

    // Read Product(s)

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // Create Product


    @PostMapping
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestParam("productFile") MultipartFile file, @Valid @RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productService.save(productDTO, file));
    }

    // Update Product

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct (@Valid @RequestBody ProductDTO productDTO  ){
        return ResponseEntity.ok(productService.update(productDTO));
    }

    // Delete Product

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct (@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}