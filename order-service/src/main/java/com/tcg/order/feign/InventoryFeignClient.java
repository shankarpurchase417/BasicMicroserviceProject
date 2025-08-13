package com.tcg.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcg.order.dto.InventoryDto;

@FeignClient(name = "inventory-service", url = "http://localhost:8084/inventory", fallback = InventoryFeignFallback.class)
	public interface InventoryFeignClient {

	    @GetMapping("/product/{productId}")
	    InventoryDto getInventoryByProductId(@PathVariable("productId") int productId);

	    @PutMapping("/update")
	    InventoryDto updateInventory(@RequestBody InventoryDto inventoryDto);
	}



