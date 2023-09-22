import com.fairgoods.webshop.dto.ProductDTO;
import com.fairgoods.webshop.controller.ProductController;
import com.fairgoods.webshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetProducts() throws Exception {
        // Mocking des ProductService, um die findAll-Methode zu überschreiben
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, "Product A", "Description A", 10.0, 5));
        productList.add(new ProductDTO(2L, "Product B", "Description B", 20.0, 10));

        when(productService.findAll()).thenReturn(productList);

        // Durchführung des MockMvc-Tests
        mockMvc.perform(MockMvcRequestBuilders.get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productname").value("Product A"))
                .andExpect(jsonPath("$[1].productname").value("Product B"));
    }

    // Fügen Sie hier weitere Testfälle für die anderen Methoden des ProductController hinzu.
}
