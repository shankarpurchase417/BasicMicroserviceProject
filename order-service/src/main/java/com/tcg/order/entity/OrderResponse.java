package com.tcg.order.entity;

import com.tcg.order.dto.OrderDto;
import com.tcg.order.dto.ProductDto;
import com.tcg.order.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse 
{
	
	private OrderDto order;
	private UserDto user;
	private ProductDto product;
	private String inventoryStatus;
	private double subtotal;


}

