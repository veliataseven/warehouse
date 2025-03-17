package com.ikea.warehouse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.dto.InventoryWrapperDTO;
import com.ikea.warehouse.model.Product;
import com.ikea.warehouse.repository.InventoryRepository;
import com.ikea.warehouse.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportServiceImpl implements DataImportService {

    private static final Logger logger = LoggerFactory.getLogger(DataImportServiceImpl.class);

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public DataImportServiceImpl(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    @Override
    public void loadArticlesFromFile(String filePath) {
        logger.info("Loading articles from file: {}", filePath);
        try {
            InventoryWrapperDTO inventoryWrapperDTO = readInventoryFile(filePath);
            List<Article> articles = inventoryWrapperDTO.getInventory();
            saveArticles(articles);
        } catch (IOException e) {
            logger.error("Error loading articles from file", e);
            throw new RuntimeException("Error loading articles: " + e.getMessage());  // Re-throw exception to be handled in controller
        }
    }

    @Transactional
    @Override
    public void loadProductsFromFile(String filePath) {
        logger.info("Loading products from file: {}", filePath);
        try {
            JsonNode rootNode = readProductFile(filePath);
            JsonNode productsNode = rootNode.get("products");
            processProducts(productsNode);
        } catch (IOException e) {
            logger.error("Error loading products from file", e);
            throw new RuntimeException("Error loading products: " + e.getMessage());  // Re-throw exception to be handled in controller
        }
    }

    private InventoryWrapperDTO readInventoryFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), InventoryWrapperDTO.class);
    }

    private void saveArticles(List<Article> articles) {
        inventoryRepository.saveAll(articles);
        logger.info("Successfully loaded articles into the database.");
    }

    private JsonNode readProductFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(filePath));
    }

    private void processProducts(JsonNode productsNode) {
        productsNode.forEach(productNode -> {
            Product product = createProductFromJson(productNode);
            productRepository.save(product);
        });
        logger.info("Successfully loaded products into the database.");
    }

    private Product createProductFromJson(JsonNode productNode) {
        Product product = new Product();
        product.setName(productNode.get("name").asText());
        product.setArticles(new ArrayList<>());  // Initialize articles list

        JsonNode articlesNode = productNode.get("contain_articles");
        processArticles(articlesNode, product);

        return product;
    }

    private void processArticles(JsonNode articlesNode, Product product) {
        articlesNode.forEach(articleNode -> {
            String artId = articleNode.get("art_id").asText();
            int amount = articleNode.get("amount_of").asInt();

            Article article = findArticleById(artId);
            if (article != null) {
                product.getArticles().add(article);
                product.getArticleQuantities().put(artId, amount);
            } else {
                logger.error("Article with artId {} not found in the database", artId);
            }
        });
    }

    private Article findArticleById(String artId) {
        return inventoryRepository.findByArtId(artId);
    }
}
