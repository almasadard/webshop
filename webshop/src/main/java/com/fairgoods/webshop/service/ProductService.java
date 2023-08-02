package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }



    public Product save (Product product){
        return productRepository.save(product);
    }

    public Product update (Product updatedProduct){
        Product product = productRepository.findById(updatedProduct.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + updatedProduct.getId() + " not found"));

        product.setId(updatedProduct.getId());
        product.setDescription(updatedProduct.getDescription());
        product.setProductname(updatedProduct.getProductname());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        return productRepository.save(product);
    }

    public void deleteById (Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }

}