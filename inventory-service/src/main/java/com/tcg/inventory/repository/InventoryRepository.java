package com.tcg.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tcg.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	
    Inventory findByProductId(int productId);
}

