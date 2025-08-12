package com.tcg.product.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int productId;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String category;
}

