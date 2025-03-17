package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<List<ProductDTO>> getAvailableProducts();

    ResponseEntity<ProductDTO> sellProduct(String productName);
}
