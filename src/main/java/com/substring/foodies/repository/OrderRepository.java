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
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByRestaurantId(String restaurantId);
    List<Order> findByUserId(String userId);
    List<Order> findByDeliveryBoyId(String userId);

}
