package com.substring.foodies.service;

import com.substring.foodies.dto.FoodItemsDto;
import com.substring.foodies.entity.FoodItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {

    FoodItemsDto addFood(FoodItemsDto foodItemsDto);

    FoodItemsDto updateFood(FoodItemsDto foodItemsDto, Long id);

    void deleteFood(Long id);

    Page<FoodItemsDto> getAllFoodItems(Pageable pageable);

    List<FoodItemsDto> getFoodByRestaurant(String restoId);

    List<FoodItemsDto> findByRestaurantIdAndCategory(String restoId, String category);

    FoodItemsDto getFoodById(Long foodId);
}
