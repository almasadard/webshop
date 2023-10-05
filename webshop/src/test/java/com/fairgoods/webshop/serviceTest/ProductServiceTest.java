package com.fairgoods.webshop.serviceTest;
import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.model.Product;
import com.fairgoods.webshop.repository.ProductRepository;
import com.fairgoods.webshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllProducts() {
        // Mocking des ProductRepository, um die findAll-Methode zu überschreiben
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Product A", "Description A", 10.0, 5));
        productList.add(new Product(2L, "Product B", "Description B", 20.0, 10));

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindProductById() {
        // Mocking des ProductRepository, um die findById-Methode zu überschreiben
        Product product = new Product(1L, "Product A", "Description A", 10.0, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product A", result.get().getProductname());
    }


}



