package com.substring.foodies.controller;


import com.substring.foodies.dto.OrderDto;
import com.substring.foodies.dto.OrderPlaceRequest;
import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    private ResponseEntity<OrderDto> placeOrder(@RequestBody OrderPlaceRequest orderPlaceRequest) {
        System.out.println("orderPlaceRequest: " + orderPlaceRequest);
        OrderDto orderDto = orderService.placeOrderRequest(orderPlaceRequest);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDto>> getOrdersByRestaurant(@PathVariable String restaurantId) {
        List<OrderDto> orders = orderService.getOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable String userId) {
        List<OrderDto> orders = orderService.getOrderByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/delivery/{deliveryBoyId}")
    public ResponseEntity<List<OrderDto>> getOrdersByDeliveryBoy(@PathVariable String deliveryBoyId) {
        List<OrderDto> orders = orderService.getOrderByDeliveryBoy(deliveryBoyId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/track")
    public ResponseEntity<OrderDto> trackOrder(@PathVariable int orderId) {
        OrderDto orderDto = orderService.trackOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable int orderId) {
        OrderDto orderItemDto = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(orderItemDto);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable int orderId,
            @RequestBody OrderStatus orderStatus) {
        OrderDto orderDto = orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(orderDto);
    }
}