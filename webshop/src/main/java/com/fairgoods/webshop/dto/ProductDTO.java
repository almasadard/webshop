package com.fairgoods.webshop.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {


    private Long id;
    private String productname;
    private String description;
    private double price;
    private int quantity;
    private String imageUrl;
    private String categoryName;
}
