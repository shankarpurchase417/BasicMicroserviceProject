package com.tcg.inventory.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
	
    private int productId;
    
    private int quantity;
}

