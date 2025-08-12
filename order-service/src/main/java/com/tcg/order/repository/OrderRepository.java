package com.tcg.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcg.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	
}
