package com.ikea.warehouse.service;

public interface DataImportService {

    void loadProductsFromFile(String filePath);

    void loadArticlesFromFile(String filePath);
}
