package com.ikea.warehouse.service;

import com.ikea.warehouse.dto.ProductDTO;
import com.ikea.warehouse.exception.InsufficientStockException;
import com.ikea.warehouse.exception.ProductNotFoundException;
import com.ikea.warehouse.mapper.DTOMapper;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.model.Product;
import com.ikea.warehouse.repository.InventoryRepository;
import com.ikea.warehouse.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAvailableProducts() {
        logger.info("Fetching all available products.");
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();

        if (products.isEmpty()) {
            logger.warn("No products available.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        for (Product product : products) {
            int availableQuantity = calculateAvailableQuantity(product);
            ProductDTO productDTO = DTOMapper.toProductDTO(product);
            productDTO.setAvailableQuantity(availableQuantity);
            productDTOs.add(productDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(productDTOs);
    }

    private int calculateAvailableQuantity(Product product) {
        int availableQuantity = Integer.MAX_VALUE;

        for (Article article : product.getArticles()) {
            int requiredAmount = product.getArticleQuantities().get(article.getArtId());
            int availableStock = article.getStock();
            int availableForThisArticle = availableStock / requiredAmount;
            availableQuantity = Math.min(availableQuantity, availableForThisArticle);
        }

        return availableQuantity;
    }

    @Override
    public ResponseEntity<ProductDTO> sellProduct(String productName) {
        logger.info("Attempting to sell product: {}", productName);
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> {
                    logger.error("Product not found: {}", productName);
                    return new ProductNotFoundException("Product not found: " + productName);
                });

        List<Article> inventory = inventoryRepository.findAll();
        checkStockAndUpdateInventory(product, inventory);

        inventoryRepository.saveAll(inventory);
        logger.info("Product sold successfully: {}", productName);

        return ResponseEntity.status(HttpStatus.OK).body(DTOMapper.toProductDTO(product));
    }

    private void checkStockAndUpdateInventory(Product product, List<Article> inventory) {
        for (Article article : product.getArticles()) {
            int amountRequired = product.getArticleQuantities().get(article.getArtId());
            Article stockArticle = findArticleInInventory(article, inventory);

            if (stockArticle.getStock() < amountRequired) {
                logger.error("Not enough stock for article: {}", article.getArtId());
                throw new InsufficientStockException("Not enough stock for article: " + article.getArtId());
            }

            stockArticle.setStock(stockArticle.getStock() - amountRequired);
        }
    }

    private Article findArticleInInventory(Article article, List<Article> inventory) {
        return inventory.stream()
                .filter(a -> a.getArtId().equals(article.getArtId()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Article not found in inventory: {}", article.getArtId());
                    return new ProductNotFoundException("Article not found in inventory: " + article.getArtId());
                });
    }
}
