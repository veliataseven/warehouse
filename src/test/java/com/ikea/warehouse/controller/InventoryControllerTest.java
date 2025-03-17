package com.ikea.warehouse.controller;

import com.ikea.warehouse.dto.ArticleDTO;
import com.ikea.warehouse.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    public void testGetAllArticles_Success() throws Exception {
        // Arrange: create a mock ArticleDTO and return it as a response
        ArticleDTO articleDTO = new ArticleDTO("1", "leg", 12);
        List<ArticleDTO> articleDTOList = Arrays.asList(articleDTO);
        when(inventoryService.getInventory()).thenReturn(ResponseEntity.ok(articleDTOList));

        // Act & Assert: verify the response from the controller is correct
        mockMvc.perform(get("/api/inventory/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].artId").value("1"))
                .andExpect(jsonPath("$[0].name").value("leg"))
                .andExpect(jsonPath("$[0].stock").value(12));

        // Verify that service method was called
        verify(inventoryService).getInventory();
    }

    @Test
    public void testGetAllArticles_NotFound() throws Exception {
        // Arrange: return a ResponseEntity with status 404 (not found)
        when(inventoryService.getInventory()).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

        // Act & Assert: verify the response is 404 status
        mockMvc.perform(get("/api/inventory/"))
                .andExpect(status().isNotFound());

        // Verify that service method was called
        verify(inventoryService).getInventory();
    }
}
