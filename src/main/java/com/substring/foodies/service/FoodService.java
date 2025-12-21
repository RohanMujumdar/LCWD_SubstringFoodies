package com.substring.foodies.service;

import com.substring.foodies.dto.FoodCategoryDto;
import com.substring.foodies.dto.FoodItemsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {

    FoodItemsDto addFood(FoodItemsDto foodItemsDto);

    FoodItemsDto updateFood(FoodItemsDto foodItemsDto, String id);

    FoodItemsDto patchFood(String id, FoodItemsDto patchDto);

    void deleteFood(String id);

    Page<FoodItemsDto> getAllFoodItems(Pageable pageable);

    List<FoodCategoryDto> getFoodByRestaurant(String restoId);

    List<FoodItemsDto> findByRestaurantIdAndFoodCategory(String restoId, String foodCategory);

    List<FoodItemsDto> findByRestaurantIdAndFoodSubCategory(String restoId, String foodSubCategory);

    List<FoodItemsDto> findByRestaurantIdAndFoodType(String restoId, String foodType);

    List<FoodItemsDto> findByFoodCategory(String foodCategory);

    List<FoodItemsDto> findByFoodSubCategory(String foodSubCategory);

    List<FoodItemsDto> findByFoodType(String foodType);

    List<FoodItemsDto> findByFoodName(String foodName);

    List<FoodItemsDto> findByRestaurantIdAndFoodName(String restoId, String foodName);

    FoodItemsDto getFoodById(String foodId);

    void addRestoForFood(String foodId, List<String> restoIds);

    void deleteRestoForFood(String foodId, List<String> restoIds);

    void updateFoodRating(String foodId, double rating);
}
