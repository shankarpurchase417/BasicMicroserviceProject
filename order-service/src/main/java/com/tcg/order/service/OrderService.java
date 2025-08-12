package com.tcg.order.service;
import com.tcg.order.dto.OrderDto;
import com.tcg.order.entity.OrderResponse;


public interface OrderService {

	OrderResponse createOrder(OrderDto orderdto);

    
        
}

