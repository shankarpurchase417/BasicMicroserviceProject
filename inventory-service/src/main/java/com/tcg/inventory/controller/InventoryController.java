package com.tcg.inventory.controller;

import com.tcg.inventory.dto.InventoryDto;
import com.tcg.inventory.service.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryDto> addInventory(@RequestBody InventoryDto inventoryDto) {
        return ResponseEntity.ok(inventoryService.addInventory(inventoryDto));
    }

    @GetMapping("/status/{productId}")
    public ResponseEntity<String> getStockStatus(@PathVariable int productId) {
        return ResponseEntity.ok(inventoryService.getStockStatus(productId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryDto> getInventory(@PathVariable int productId) {
        InventoryDto dto = inventoryService.getInventoryByProductId(productId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/deduct")
    public ResponseEntity<String> deductStock(@RequestBody InventoryDto dto) {
        boolean result = inventoryService.deductStock(dto);
        return result ? ResponseEntity.ok("Stock Deducted")
                      : ResponseEntity.badRequest().body("Insufficient stock");
    }
    
    @PutMapping("/update")
    public ResponseEntity<InventoryDto> updateInventory(@RequestBody InventoryDto inventoryDto) {
        return ResponseEntity.ok(inventoryService.updateInventory(inventoryDto));
    }


}

