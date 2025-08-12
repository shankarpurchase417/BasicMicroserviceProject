package com.tcg.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	
	
	  
    private String item;
    private int quantity;
    private int userId;
    private int productId;


}
