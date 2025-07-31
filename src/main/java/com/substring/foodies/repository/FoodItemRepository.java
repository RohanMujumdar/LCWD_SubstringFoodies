package com.substring.foodies.repository;

import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItems, Long> {

    List<FoodItems> findByRestaurant(Restaurant restaurant);
}
