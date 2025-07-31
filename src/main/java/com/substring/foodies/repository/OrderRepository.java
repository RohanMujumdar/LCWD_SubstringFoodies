package com.substring.foodies.repository;

import com.substring.foodies.entity.Order;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByUser(User user);
    List<Order> findByDeliveryBoy(User deliveryBoy);
}
