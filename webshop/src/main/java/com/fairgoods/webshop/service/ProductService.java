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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves all products, mapping each product to a ProductDTO.
     *
     * @return A list of ProductDTO objects representing all products.
     */
    public List<ProductDTO> findAll(){
        return productRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a product by its id and maps it to a ProductDTO.
     *
     * @param id The id of the product to find.
     * @return A ProductDTO object representing the found product.
     * @throws ObjectNotFoundException if the product is not found.
     */
    public ProductDTO findById(Long id){
        var findProductById = productRepository.findById(id);
        if (findProductById.isEmpty()) {
            throw new ObjectNotFoundException(findProductById, "Product not found");
        }
        return toDTO(findProductById.get());
    }

    /**
     * Saves a new product or updates an existing product.
     *
     * @param product The product entity to save.
     * @return The saved product entity.
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product's information.
     *
     * @param updatedProductDTO A ProductDTO containing the updated information.
     * @return A ProductDTO object representing the updated product.
     * @throws EntityNotFoundException if the product is not found.
     */
    public ProductDTO update (ProductDTO updatedProductDTO){
        Product product = productRepository.findById(updatedProductDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + updatedProductDTO.getId() + " not found"));

        product.setId(updatedProductDTO.getId());
        product.setDescription(updatedProductDTO.getDescription());
        product.setProductname(updatedProductDTO.getProductname());
        product.setPrice(updatedProductDTO.getPrice());
        product.setImageUrl(updatedProductDTO.getImageUrl());
        product.setQuantity(updatedProductDTO.getQuantity());

        return toDTO(productRepository.save(product));
    }

    /**
     * Deletes a product by its id.
     *
     * @param id The id of the product to delete.
     * @throws EntityNotFoundException if the product is not found.
     */
    public void deleteById (Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }

    /**
     * Maps a Product entity to a ProductDTO.
     *
     * @param product The Product entity to map.
     * @return A ProductDTO object representing the mapped product.
     */
    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getProductname(), product.getDescription(), product.getPrice(), product.getQuantity(), product.getImageUrl(), product.getCategory().getName());
    }

    /**
     * Maps a ProductDTO to a Product entity.
     *
     * @param productDTO The ProductDTO to map.
     * @return A Product entity representing the mapped ProductDTO.
     * @throws IllegalArgumentException if the category is not found.
     */
    public Product toEntity(ProductDTO productDTO) {
        Category category = categoryRepository.findByName(productDTO.getCategoryName());
        if (category == null) {
            throw new IllegalArgumentException("No category found with name: " + productDTO.getCategoryName());
        }
        return new Product(productDTO.getId(), productDTO.getProductname(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getQuantity(), productDTO.getImageUrl(), category);
    }

}