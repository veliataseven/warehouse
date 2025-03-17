package com.ikea.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String name;
    private double price;
    private List<ArticleDTO> containArticles;
    private int availableQuantity;

    public ProductDTO(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
