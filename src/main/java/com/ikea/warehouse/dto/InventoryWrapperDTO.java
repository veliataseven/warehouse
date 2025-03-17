package com.ikea.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ikea.warehouse.model.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryWrapperDTO {

    @JsonProperty("inventory")
    private List<Article> inventory;
}