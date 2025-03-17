package com.ikea.warehouse.repository;

import com.ikea.warehouse.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Article, String> {
    Article findByArtId(String artId);
}
