package com.substring.foodies.repository;

import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FoodItemRepository extends JpaRepository<FoodItems, Long> {

    List<FoodItems> findByRestaurantId(String id);
}
