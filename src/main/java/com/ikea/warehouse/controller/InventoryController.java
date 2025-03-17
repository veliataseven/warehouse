package com.ikea.warehouse.controller;

import com.ikea.warehouse.dto.ArticleDTO;
import com.ikea.warehouse.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        logger.info("Received request to get all articles in the inventory.");

        // Call service method to get inventory
        return inventoryService.getInventory();
    }
}
