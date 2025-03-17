package com.ikea.warehouse.controller;

import com.ikea.warehouse.dto.ProductDTO;
import com.ikea.warehouse.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetAvailableProducts() throws Exception {
        // Given
        ProductDTO productDTO = new ProductDTO("Dining Chair", 100.0);
        when(productService.getAvailableProducts()).thenReturn(ResponseEntity.ok(java.util.Collections.singletonList(productDTO)));

        // When & Then
        mockMvc.perform(get("/api/products/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dining Chair"))
                .andExpect(jsonPath("$[0].price").value(100.0));
    }

    @Test
    public void testSellProduct() throws Exception {
        // Given
        String productName = "Dining Chair";
        ProductDTO productDTO = new ProductDTO("Dining Chair", 100.0);
        when(productService.sellProduct(productName)).thenReturn(ResponseEntity.ok(productDTO));

        // When & Then
        mockMvc.perform(post("/api/products/sell/{productName}", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dining Chair"))
                .andExpect(jsonPath("$.price").value(100.0));

        // Verify service method is called
        verify(productService, times(1)).sellProduct(productName);
    }
}
