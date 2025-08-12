package com.tcg.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
	
	 private int inventoryId;
	    private int productId;
	    private int quantity;

}
