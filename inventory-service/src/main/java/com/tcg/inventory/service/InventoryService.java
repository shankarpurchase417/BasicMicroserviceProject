package com.tcg.inventory.service;

import com.tcg.inventory.dto.InventoryDto;

public interface InventoryService {
	
    InventoryDto addInventory(InventoryDto inventoryDto);
    
    String getStockStatus(int productId);
    
    InventoryDto getInventoryByProductId(int productId);
    
    boolean deductStock(InventoryDto dto);
    
    InventoryDto updateInventory(InventoryDto inventoryDto);


}

