package com.tcg.order.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcg.order.dto.OrderDto;
import com.tcg.order.entity.OrderResponse;
import com.tcg.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderDto orderdto) {
        OrderResponse resp = orderService.createOrder(orderdto);
        return ResponseEntity.ok(resp);
    }
}

