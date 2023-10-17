package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.service.LocalFileService;
import com.fairgoods.webshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final LocalFileService localFileService;

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


    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@ModelAttribute ProductDTO productDTO,
                                                    @RequestParam("file") MultipartFile file){
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