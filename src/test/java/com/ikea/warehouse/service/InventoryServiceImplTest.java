package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ArticleDTO;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryServiceImplTest {

    private InventoryServiceImpl inventoryService;
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        inventoryRepository = mock(InventoryRepository.class);
        inventoryService = new InventoryServiceImpl(inventoryRepository);
    }

    @Test
    public void testGetInventory_ArticlesFound() {
        // Arrange
        Article article1 = new Article();
        article1.setArtId("1");
        article1.setName("leg");
        article1.setStock(12);

        Article article2 = new Article();
        article2.setArtId("2");
        article2.setName("screw");
        article2.setStock(17);

        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        // Act
        ResponseEntity<List<ArticleDTO>> response = inventoryService.getInventory();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("leg", response.getBody().get(0).getName());
        assertEquals(12, response.getBody().get(0).getStock());
        assertEquals("screw", response.getBody().get(1).getName());
        assertEquals(17, response.getBody().get(1).getStock());
    }

    @Test
    public void testGetInventory_NoArticlesFound() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<ArticleDTO>> response = inventoryService.getInventory();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());  // Body should be null when no articles are found
    }
}
