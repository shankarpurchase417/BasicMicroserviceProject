package com.tcg.order.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.tcg.order.dto.InventoryDto;
import com.tcg.order.dto.OrderDto;
import com.tcg.order.dto.ProductDto;
import com.tcg.order.dto.UserDto;
import com.tcg.order.entity.Order;
import com.tcg.order.entity.OrderResponse;
import com.tcg.order.feign.InventoryFeignClient;
import com.tcg.order.repository.OrderRepository;
import com.tcg.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;
    
 // ONLY for User Service call (5s read-timeout)
    @Autowired
    @Qualifier("userServiceRestTemplate")
    private RestTemplate userRt;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepo;
    
    @Autowired
    private InventoryFeignClient inventoryFeignClient;

    @Override
    public OrderResponse createOrder(OrderDto orderdto) {

        // 1. Map DTO to Entity (Id skipped automatically)
        Order order = modelMapper.map(orderdto, Order.class);

     // 2. Fetch User (5s read-timeout apply hoga)
        UserDto userdto;
        try {
            userdto = userRt.getForObject(
                "http://localhost:8081/users/" + orderdto.getUserId(),
                UserDto.class
            );
        } catch (ResourceAccessException ex) {
            // Read timeout hone par yahan aayega
            throw new ResponseStatusException(
                HttpStatus.GATEWAY_TIMEOUT,
                "User-service timed out after 5s",
                ex
            );
        }
        if (userdto == null) throw new RuntimeException("User not found");

        // 3. Fetch Product
        ProductDto productdto = restTemplate.getForObject(
            "http://localhost:8083/products/" + orderdto.getProductId(),
            ProductDto.class
        );
        if (productdto == null) throw new RuntimeException("Product not found");

        // 4. Fetch Inventory
        InventoryDto inventoryDto = restTemplate.getForObject(
            "http://localhost:8084/inventory/product/" + orderdto.getProductId(),
            InventoryDto.class
        );
        if (inventoryDto == null || inventoryDto.getQuantity() < orderdto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 5. Update Inventory
        inventoryDto.setQuantity(inventoryDto.getQuantity() - orderdto.getQuantity());
        restTemplate.put("http://localhost:8084/inventory/update", inventoryDto);

        // 6. Calculate subtotal
        double subtotal = orderdto.getQuantity() * productdto.getPrice();

        // 7. Save order
        Order savedOrder = orderRepo.save(order);
        OrderDto responseDto = modelMapper.map(savedOrder, OrderDto.class);

        // 8. Get stock status
        String stockStatus = restTemplate.getForObject(
            "http://localhost:8084/inventory/status/" + orderdto.getProductId(),
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

	@Override
	public String cancelOrder(int orderId) {
		// 1. Order fetch karo
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Product ID and Quantity nikalo
        int productId = order.getProductId();
        int orderedQty = order.getQuantity();

        // 3. Current inventory fetch karo (Feign se)
        InventoryDto currentInventory = inventoryFeignClient.getInventoryByProductId(productId);
        if (currentInventory == null) {
            throw new RuntimeException("Unable to fetch inventory (service down?)");
        }

        // 4. Inventory quantity wapas badhao
        currentInventory.setQuantity(currentInventory.getQuantity() + orderedQty);

        // 5. Inventory update karo
        inventoryFeignClient.updateInventory(currentInventory);

        // 6. Order delete karo
        orderRepo.delete(order);

        return "Order cancelled and inventory restored successfully";
    }
}




