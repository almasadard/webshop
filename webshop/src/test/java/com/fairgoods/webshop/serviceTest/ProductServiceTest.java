package com.fairgoods.webshop.serviceTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Category;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.repository.CategoryRepository;
import com.fairgoods.webshop.repository.ProductRepository;
import com.fairgoods.webshop.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup(){

        Category category1 = new Category();
        category1.setName("Category1");
        Product product1 = new Product();
        product1.setProductname("Product 1");
        product1.setDescription("This is Product1");
        product1.setPrice(1);
        product1.setQuantity(1);
        product1.setCategory(category1);

        Category category2 = new Category();
        category2.setName("Category2");
        Product product2 = new Product();
        product2.setProductname("Product 2");
        product2.setDescription("This is Product2");
        product2.setPrice(2);
        product2.setQuantity(2);
        product2.setCategory(category2);

        Category category3 = new Category();
        category3.setName("Category3");
        Product product3 = new Product();
        product3.setProductname("Product 3");
        product3.setDescription("This is Product3");
        product3.setPrice(3);
        product3.setQuantity(3);
        product3.setCategory(category3);

        categoryRepository.saveAll(List.of(category1, category2, category3));
        productRepository.saveAll(List.of(product1,product2,product3));
    }

    @AfterEach
    void clean() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void findAllTest() {
        List<ProductDTO> products = assertDoesNotThrow(() -> productService.findAll());
        assertNotNull(products);
        assertEquals(3, products.size());
    }

    @Test
    void findByIdTest() {
        List<ProductDTO> products = assertDoesNotThrow(() -> productService.findAll());
        final Long productId = products.stream()
                .filter(p -> p.getProductname().equals("Product 1"))
                .findFirst()
                .get()
                .getId();
        ProductDTO productDTO = assertDoesNotThrow(() -> productService.findById(productId));

        // Überprüft alle und zeigt mir am Ende jene, die nicht passen - testet aber alle anderen ohne abzubrechen
        assertAll("ProductFields",
                () -> assertEquals("Product 1", productDTO.getProductname()),
                () -> assertEquals("This is Product1", productDTO.getDescription()),
                //() -> assertNotEquals("This is Product1", productDTO.getDescription()),
                () -> assertEquals(1, productDTO.getPrice()),
                () -> assertEquals(1, productDTO.getQuantity()),
                () -> assertEquals("Category1", productDTO.getCategoryName())
        );
    }
}
