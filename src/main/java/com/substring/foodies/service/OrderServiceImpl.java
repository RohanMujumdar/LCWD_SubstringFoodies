package com.substring.foodies.service;

import com.substring.foodies.dto.OrderDto;
import com.substring.foodies.dto.OrderPlaceRequest;
import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.dto.enums.PaymentMode;
import com.substring.foodies.dto.enums.PaymentStatus;
import com.substring.foodies.entity.*;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.CartRepository;
import com.substring.foodies.repository.OrderRepository;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public OrderDto placeOrderRequest(OrderPlaceRequest orderPlaceRequest) {

        User user = userRepository.findById(orderPlaceRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFound(String.format("User not found with id = %s", orderPlaceRequest.getUserId())));
        Cart cart = cartRepository.findByCreator(user).orElseThrow(() -> new ResourceNotFound(String.format("Cart Not Found for userId = %s", user.getId())));

        Restaurant restaurant = restaurantRepository.findById(orderPlaceRequest.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFound(String.format("Restaurant not found with id = %s", orderPlaceRequest.getRestaurantId())));

        List<CartItems> cartItems = cart.getCartItems();
        System.out.println("size of the cart: " + cartItems.size());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        // Convert cart items to order items
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);

        Address address = mapper.map(orderPlaceRequest.getAddress(), Address.class);
        if (address.getId() == null)
            address.setId(null);

        order.setAddress(address);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderedAt(LocalDateTime.now());
        order.setPaymentMode(orderPlaceRequest.getPaymentMode());

        if(orderPlaceRequest.getPaymentMode().equals(PaymentMode.CASH_ON_DELIVERY))
        {
            order.setPaymentStatus(PaymentStatus.NOT_PAID);
        }
        else
        {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        final AtomicInteger totalAmount = new AtomicInteger(0);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setFoodItems(cartItem.getFoodItems());
//                    totalAmount.set(totalAmount.get()
//                            +( (int)( cartItem.getFoodItems().actualPrice() * cartItem.getQuantity())));
                    totalAmount.set(totalAmount.get()
                            +(cartItem.getTotalCartItemsPrice()));

                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setTotalAmount(totalAmount.get());
        order.setOrderItemList(orderItems);
        orderRepository.save(order);
        System.out.println("Saved Successfully");
        // Clear user's cart
        cartService.clearCart(orderPlaceRequest.getUserId());
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByRestaurant(String restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrderByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrderByDeliveryBoy(String deliveryBoyId) {
        List<Order> orders = orderRepository.findByDeliveryBoyId(deliveryBoyId);
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto trackOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Order not found for orderId = %s", orderId)));
        return mapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Order not found for orderId = %s", orderId)));
        order.setStatus(OrderStatus.CANCELLED);

        LocalDateTime currentTime = LocalDateTime.now();

        long minutesPassed = Duration.between(order.getOrderedAt(), currentTime).toMinutes();

        if(minutesPassed > 10)
        {
            order.setPaymentStatus(PaymentStatus.REFUNDED);
        }

        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrderStatus(String orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound("Order not found with id: " + orderId));
        order.setStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDto.class);
    }

}