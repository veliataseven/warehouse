package com.ikea.warehouse.mapper;

import com.ikea.warehouse.dto.ArticleDTO;
import com.ikea.warehouse.dto.ProductDTO;
import com.ikea.warehouse.model.Article;
import com.ikea.warehouse.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static ProductDTO toProductDTO(Product product) {

        List<ArticleDTO> articleDTOList = product.getArticles().stream()
                .map(article -> new ArticleDTO(
                        article.getArtId(),
                        article.getName(),
                        product.getArticleQuantities().get(article.getArtId())))
                .collect(Collectors.toList());

        return new ProductDTO(product.getName(), product.getPrice(), articleDTOList, 0);
    }

    public static ArticleDTO toArticleDTO(Article article) {
        return new ArticleDTO(article.getArtId(), article.getName(), article.getStock());
    }

    public static List<ArticleDTO> toArticleDTOList(List<Article> articles) {
        return articles.stream().map(DTOMapper::toArticleDTO).collect(Collectors.toList());
    }
}
