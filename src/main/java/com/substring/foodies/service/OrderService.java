package com.substring.foodies.service;


import com.substring.foodies.dto.OrderDto;
import com.substring.foodies.dto.OrderItemDto;
import com.substring.foodies.dto.OrderPlaceRequest;
import com.substring.foodies.dto.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDto placeOrderRequest(OrderPlaceRequest orderPlaceRequest);

    List<OrderDto> getAllOrders();

    List<OrderDto> getOrdersByRestaurant(String restoId);

    List<OrderDto> getOrderByUserId(String userId);

    List<OrderDto> getOrderByDeliveryBoy(String id);

    OrderDto trackOrder(int orderId);

    OrderDto cancelOrder(int orderId);

    OrderDto updateOrderStatus(int orderId, OrderStatus orderStatus);
}
