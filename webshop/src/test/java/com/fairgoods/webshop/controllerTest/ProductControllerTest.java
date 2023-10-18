package com.fairgoods.webshop.controllerTest;

import com.fairgoods.webshop.WebshopApplication;
import com.fairgoods.webshop.model.Category;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.model.User;
import com.fairgoods.webshop.repository.CategoryRepository;
import com.fairgoods.webshop.repository.ProductRepository;
import com.fairgoods.webshop.repository.UserRepository;
import com.fairgoods.webshop.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WebshopApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private String token = "";

    @BeforeEach
    void setup() {

        Category category1 = new Category();
        category1.setName("Category1");
        Product product1 = new Product();
        product1.setProductname("Product 1");
        product1.setDescription("This is Product1");
        product1.setImageUrl("ImageUrlProduct1");
        product1.setPrice(1);
        product1.setQuantity(1);
        product1.setCategory(category1);

        Category category2 = new Category();
        category2.setName("Category2");
        Product product2 = new Product();
        product2.setProductname("Product 2");
        product2.setDescription("This is Product2");
        product1.setImageUrl("ImageUrlProduct2");
        product2.setPrice(2);
        product2.setQuantity(2);
        product2.setCategory(category2);

        Category category3 = new Category();
        category3.setName("Category3");
        Product product3 = new Product();
        product3.setProductname("Product 3");
        product3.setDescription("This is Product3");
        product1.setImageUrl("ImageUrlProduct3");
        product3.setPrice(3);
        product3.setQuantity(3);
        product3.setCategory(category3);

        User admin = new User();
        admin.setId(1L);
        admin.setFirstname("User1");
        admin.setLastname("UserL1");
        admin.setEmail("user1@test.com");
        admin.setPassword("*geheim1");
        admin.setAdmin(true);

        categoryRepository.saveAll(List.of(category1, category2, category3));
        productRepository.saveAll(List.of(product1, product2, product3));
        userRepository.save(admin);
        token = "Bearer " + tokenService.generateToken(admin);
    }

    @AfterEach
    void clean() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getProductsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(3)))
                .andExpect(jsonPath("$.[*]").isNotEmpty());

    }

    @Test
    void getProductByIdTest() throws Exception {
        final Product product = productRepository.findAll().stream()
                //.filter(p -> p.getId().equals(1L))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        final Long productId = product.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())

                // Prüft ob ID im JSON existiert und ob sie mit ID der Datenbank übereinstimmt
                .andExpect(jsonPath("$.id", Matchers.is(productId.intValue())));
    }


    @Test
    void deleteProductTest() throws Exception {
        final Product product = productRepository.findAll().stream()
                //.filter(p -> p.getId().equals(2L))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        final Long productId = product.getId();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/product/{id}", productId)
                        .header("Authorization", token))
                .andExpect(status().is2xxSuccessful());

    }


/*
    @Test
    void createProductTest() throws Exception {


        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductname("Product 1");
        productDTO.setDescription("This is Product1");
        productDTO.setImageUrl("ImageUrlProduct1");
        productDTO.setPrice(1);
        productDTO.setQuantity(1);
        productDTO.setCategoryName("Category1");


        String productDTOString = mapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/product")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDTOString))
                        .andExpect(status().isOk());
    }*/
}