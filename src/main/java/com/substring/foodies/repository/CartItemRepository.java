package com.substring.foodies.repository;

import com.substring.foodies.entity.Cart;
import com.substring.foodies.entity.CartItems;
import com.substring.foodies.entity.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItems, Integer> {

    List<CartItems> findByCart(Cart cart);
    CartItems findByFoodItems(FoodItems foodItems);
}
