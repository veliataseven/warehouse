package com.ikea.warehouse.controller;

import com.ikea.warehouse.service.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataImportController {

    private static final Logger logger = LoggerFactory.getLogger(DataImportController.class);

    private final DataImportService dataImportService;

    @Autowired
    public DataImportController(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    @PostMapping("/loadData")
    public ResponseEntity<String> loadData(@RequestParam String articlesFilePath, @RequestParam String productsFilePath) {
        try {
            logger.info("Received request to load articles from file: {}", articlesFilePath);
            dataImportService.loadArticlesFromFile(articlesFilePath);

            logger.info("Received request to load products from file: {}", productsFilePath);
            dataImportService.loadProductsFromFile(productsFilePath);

            // Return success response
            return ResponseEntity.status(HttpStatus.OK).body("Articles and Products loaded successfully!");
        } catch (Exception e) {
            logger.error("Error occurred while loading data: {}", e.getMessage());

            // Return error response with internal server error status code
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while loading data: " + e.getMessage());
        }
    }
}
