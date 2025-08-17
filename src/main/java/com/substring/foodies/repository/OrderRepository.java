package com.substring.foodies.repository;

import com.substring.foodies.entity.Order;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByRestaurantId(String restaurantId);
    List<Order> findByUser(User user);
    List<Order> findByUserId(String userId);
    List<Order> findByDeliveryBoy(User deliveryBoy);
    List<Order> findByDeliveryBoyId(String userId);

}
