package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ArticleDTO;
import com.ikea.warehouse.mapper.DTOMapper;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.repository.InventoryRepository;
import com.ikea.warehouse.exception.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public ResponseEntity<List<ArticleDTO>> getInventory() {
        logger.info("Fetching all articles in inventory.");
        List<Article> articles = inventoryRepository.findAll();

        // If no articles are found, throw NoDataFoundException
        if (articles.isEmpty()) {
            logger.warn("No articles found in the inventory.");
            throw new NoDataFoundException("No articles found in the inventory.");
        }

        // Convert the list of articles to a list of ArticleDTOs
        List<ArticleDTO> articleDTOList = DTOMapper.toArticleDTOList(articles);

        // Return a 200 OK response with the list of articles
        return ResponseEntity.ok(articleDTOList);
    }
}
