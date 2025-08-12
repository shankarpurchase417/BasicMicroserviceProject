package com.tcg.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
