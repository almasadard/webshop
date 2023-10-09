package com.fairgoods.webshop.service;

import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Category;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.repository.CategoryRepository;
import com.fairgoods.webshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public List<ProductDTO> findAll(){
        return productRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> findById(Long id){
        return productRepository.findById(id)
                .map(this::toDTO);
    }

    public ProductDTO save (ProductDTO productDTO){
        Product product = toEntity(productDTO);
        product = productRepository.save(product);
        return toDTO(product);
    }

    public ProductDTO update (ProductDTO updatedProductDTO){
        Product product = productRepository.findById(updatedProductDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + updatedProductDTO.getId() + " not found"));

        product.setId(updatedProductDTO.getId());
        product.setDescription(updatedProductDTO.getDescription());
        product.setProductname(updatedProductDTO.getProductname());
        product.setPrice(updatedProductDTO.getPrice());
        product.setQuantity(updatedProductDTO.getQuantity());

        return toDTO(productRepository.save(product));
    }

    public void deleteById (Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }


    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getProductname(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getCategory().getName());
    }

    public Product toEntity(ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        if (category == null) {
            throw new IllegalArgumentException("No category found with name: " + productDTO.getCategoryName());
        }
        return new Product(productDTO.getId(), productDTO.getProductname(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getQuantity(), category);
    }

}