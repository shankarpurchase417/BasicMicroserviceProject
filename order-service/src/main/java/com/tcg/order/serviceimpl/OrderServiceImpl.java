package com.tcg.order.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcg.order.dto.InventoryDto;
import com.tcg.order.dto.OrderDto;
import com.tcg.order.dto.ProductDto;
import com.tcg.order.dto.UserDto;
import com.tcg.order.entity.Order;
import com.tcg.order.entity.OrderResponse;
import com.tcg.order.repository.OrderRepository;
import com.tcg.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepo;

    @Override
    public OrderResponse createOrder(OrderDto orderdto) {

        // 1. Map DTO to Entity (Id skipped automatically)
        Order order = modelMapper.map(orderdto, Order.class);

        // 2. Fetch User
        UserDto userdto = restTemplate.getForObject(
            "http://localhost:8081/users/" + orderdto.getUserId(),
            UserDto.class
        );
        if (userdto == null) throw new RuntimeException("User not found");

        // 3. Fetch Product
        ProductDto productdto = restTemplate.getForObject(
            "http://localhost:8082/products/" + orderdto.getProductId(),
            ProductDto.class
        );
        if (productdto == null) throw new RuntimeException("Product not found");

        // 4. Fetch Inventory
        InventoryDto inventoryDto = restTemplate.getForObject(
            "http://localhost:8083/inventory/product/" + orderdto.getProductId(),
            InventoryDto.class
        );
        if (inventoryDto == null || inventoryDto.getQuantity() < orderdto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 5. Update Inventory
        inventoryDto.setQuantity(inventoryDto.getQuantity() - orderdto.getQuantity());
        restTemplate.put("http://localhost:8083/inventory/update", inventoryDto);

        // 6. Calculate subtotal
        double subtotal = orderdto.getQuantity() * productdto.getPrice();

        // 7. Save order
        Order savedOrder = orderRepo.save(order);
        OrderDto responseDto = modelMapper.map(savedOrder, OrderDto.class);

        // 8. Get stock status
        String stockStatus = restTemplate.getForObject(
            "http://localhost:8083/inventory/status/" + orderdto.getProductId(),
            String.class
        );

        // 9. Build response
        OrderResponse response = new OrderResponse();
        response.setOrder(responseDto);
        response.setUser(userdto);
        response.setProduct(productdto);
        response.setInventoryStatus(stockStatus);
        response.setSubtotal(subtotal);

        return response;
    }
}




