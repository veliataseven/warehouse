package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ProductDTO;
import com.ikea.warehouse.exception.InsufficientStockException;
import com.ikea.warehouse.exception.NoDataFoundException;
import com.ikea.warehouse.exception.ProductNotFoundException;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.model.Product;
import com.ikea.warehouse.repository.InventoryRepository;
import com.ikea.warehouse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        inventoryRepository = mock(InventoryRepository.class);

        productService = new ProductServiceImpl(productRepository, inventoryRepository);
    }

    @Test
    public void testGetAvailableProducts() {
        // Arrange
        Product product = new Product();
        product.setName("Dining Chair");

        Article article1 = new Article();
        article1.setArtId("1");
        article1.setStock(10);

        Article article2 = new Article();
        article2.setArtId("2");
        article2.setStock(5);

        product.setArticles(Arrays.asList(article1, article2));
        product.getArticleQuantities().put("1", 2);
        product.getArticleQuantities().put("2", 3);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act
        ResponseEntity<List<ProductDTO>> response = productService.getAvailableProducts();

        // Assert
        assertEquals(200, response.getStatusCodeValue());  // OK status
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Dining Chair", response.getBody().get(0).getName());
        assertEquals(1, response.getBody().get(0).getAvailableQuantity());  // 5/3 = 1 available quantity
    }

    @Test
    public void testGetAvailableProducts_NoDataFoundException() {
        // Arrange: return empty list of products
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        NoDataFoundException thrown = assertThrows(NoDataFoundException.class, () -> {
            productService.getAvailableProducts();
        });

        assertEquals("No products available.", thrown.getMessage());
    }

    @Test
    public void testSellProduct_Success() {
        // Arrange
        Product product = new Product();
        product.setName("Dining Chair");

        Article article1 = new Article();
        article1.setArtId("1");
        article1.setStock(10);

        Article article2 = new Article();
        article2.setArtId("2");
        article2.setStock(5);

        product.setArticles(Arrays.asList(article1, article2));
        product.getArticleQuantities().put("1", 2);
        product.getArticleQuantities().put("2", 3);

        when(productRepository.findByName("Dining Chair")).thenReturn(Optional.of(product));
        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        // Act
        ResponseEntity<ProductDTO> response = productService.sellProduct("Dining Chair");

        // Assert
        assertEquals(200, response.getStatusCodeValue());  // OK status
        assertNotNull(response.getBody());
        assertEquals("Dining Chair", response.getBody().getName());
        assertEquals(8, article1.getStock());  // Stock after sell: 10 - 2 = 8
        assertEquals(2, article2.getStock());  // Stock after sell: 5 - 3 = 2
    }

    @Test
    public void testSellProduct_InsufficientStockException() {
        // Arrange
        Product product = new Product();
        product.setName("Dining Chair");

        Article article1 = new Article();
        article1.setArtId("1");
        article1.setStock(2);

        Article article2 = new Article();
        article2.setArtId("2");
        article2.setStock(1);

        product.setArticles(Arrays.asList(article1, article2));
        product.getArticleQuantities().put("1", 3);  // Need more than available stock
        product.getArticleQuantities().put("2", 2);  // Need more than available stock

        when(productRepository.findByName("Dining Chair")).thenReturn(Optional.of(product));
        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        // Act & Assert
        InsufficientStockException thrown = assertThrows(InsufficientStockException.class, () -> {
            productService.sellProduct("Dining Chair");
        });

        assertEquals("Not enough stock for article: 1", thrown.getMessage());
    }

    @Test
    public void testSellProduct_ProductNotFoundException() {
        // Arrange
        when(productRepository.findByName("Nonexistent Product")).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class, () -> {
            productService.sellProduct("Nonexistent Product");
        });

        assertEquals("Product not found: Nonexistent Product", thrown.getMessage());
    }
}
