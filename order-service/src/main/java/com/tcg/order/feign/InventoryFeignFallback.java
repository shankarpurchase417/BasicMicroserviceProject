package com.tcg.order.feign;

import org.springframework.stereotype.Component;

import com.tcg.order.dto.InventoryDto;

@Component
public class InventoryFeignFallback implements InventoryFeignClient {

    @Override
    public InventoryDto getInventoryByProductId(int productId) {
        System.out.println("Inventory service down: Cannot fetch inventory for product " + productId);
        return null; // ya default InventoryDto return kar sakte hain
    }

    @Override
    public InventoryDto updateInventory(InventoryDto inventoryDto) {
        System.out.println("Inventory service down: Cannot update inventory");
        return null;
    }
}

