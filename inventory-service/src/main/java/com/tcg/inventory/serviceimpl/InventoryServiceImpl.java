package com.tcg.inventory.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcg.inventory.dto.InventoryDto;
import com.tcg.inventory.entity.Inventory;
import com.tcg.inventory.repository.InventoryRepository;
import com.tcg.inventory.service.InventoryService;
import org.modelmapper.ModelMapper;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InventoryDto addInventory(InventoryDto inventoryDto) {
    	 
        Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
        inventory.setInventoryId(null); 
        Inventory saved = inventoryRepo.save(inventory);
        return modelMapper.map(saved, InventoryDto.class);
    }

    @Override
    public String getStockStatus(int productId) {
        Inventory inventory = inventoryRepo.findByProductId(productId);
        if (inventory == null) return "No Stock Info";
        if (inventory.getQuantity() <= 5) return "Limited Stock";
        return "Available";
    }

    @Override
    public InventoryDto getInventoryByProductId(int productId) {
        Inventory inventory = inventoryRepo.findByProductId(productId);
        return inventory != null ? modelMapper.map(inventory, InventoryDto.class) : null;
    }
    
    @Override
    public boolean deductStock(InventoryDto dto) {
        Inventory inventory = inventoryRepo.findByProductId(dto.getProductId());
        if (inventory == null || inventory.getQuantity() < dto.getQuantity()) {
            return false; // insufficient stock
        }

        inventory.setQuantity(inventory.getQuantity() - dto.getQuantity());
        inventoryRepo.save(inventory);
        return true;
    }
    
    @Override
    public InventoryDto updateInventory(InventoryDto inventoryDto) {
        Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
        Inventory updated = inventoryRepo.save(inventory);
        return modelMapper.map(updated, InventoryDto.class);
    }


}

