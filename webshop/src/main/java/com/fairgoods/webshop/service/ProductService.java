package com.fairgoods.webshop.service;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Category;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.repository.CategoryRepository;
import com.fairgoods.webshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Verzeichnis zum Hochladen von Bildern
    private final Path uploadDirectory = Paths.get("/Users/almas/OneDrive/Desktop/webshop/webshop/src/main/java/com/fairgoods/webshop/images");

    public List<ProductDTO> findAll(){
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id){
        var findProductById = productRepository.findById(id);
        if (findProductById.isEmpty()) {
            throw new ObjectNotFoundException(findProductById, "Produkt nicht gefunden");
        }
        return toDTO(findProductById.get());
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(ProductDTO updatedProductDTO){
        Product product = productRepository.findById(updatedProductDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + updatedProductDTO.getId() + " not found"));

        product.setDescription(updatedProductDTO.getDescription());
        product.setProductname(updatedProductDTO.getProductname());
        product.setPrice(updatedProductDTO.getPrice());
        product.setImageUrl(updatedProductDTO.getImageUrl());
        product.setQuantity(updatedProductDTO.getQuantity());

        return productRepository.save(product);
    }

    public void deleteById (Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getProductname(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getImageUrl(), product.getCategory().getName());
    }

    public Product toEntity(ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        if (category == null) {
            throw new IllegalArgumentException("No category found with name: " + productDTO.getCategoryName());
        }
        return new Product(productDTO.getId(), productDTO.getProductname(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getQuantity(), productDTO.getImageUrl(), category);
    }
}
