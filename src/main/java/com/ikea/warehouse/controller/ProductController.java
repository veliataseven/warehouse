package com.ikea.warehouse.controller;

import com.ikea.warehouse.dto.ProductDTO;
import com.ikea.warehouse.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductDTO>> getAvailableProducts() {
        logger.info("Fetching available products.");
        return productService.getAvailableProducts();
    }

    @PostMapping("/sell/{productName}")
    public ResponseEntity<ProductDTO> sellProduct(@PathVariable String productName) {
        logger.info("Received request to sell product: {}", productName);
        return productService.sellProduct(productName);
    }
}
