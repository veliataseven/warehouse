package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ArticleDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {

    ResponseEntity<List<ArticleDTO>> getInventory();
}
