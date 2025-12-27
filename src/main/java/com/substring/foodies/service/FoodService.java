package com.substring.foodies.service;

import com.substring.foodies.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {

    // ---------- CREATE / UPDATE ----------
    FoodItemsMenuDto addFood(FoodItemRequestDto foodItemRequestDto);

    FoodItemsMenuDto updateFood(FoodItemRequestDto foodItemRequestDto, String id);

    FoodItemsMenuDto patchFood(String id, FoodItemsMenuDto patchDto);

    // ---------- DELETE ----------
    void deleteFood(String id);

    // ---------- ADMIN / DETAILS ----------
    Page<FoodItemDetailsDto> getAllFoodItems(Pageable pageable);

    FoodItemDetailsDto getFoodById(String foodId);

    // ---------- MENU / USER ----------
    List<FoodCategoryDto> getFoodByRestaurant(String restoId);

    // ---------- FILTERS ----------
    List<FoodItemDetailsDto> findByRestaurantIdAndFoodCategory(String restoId, String foodCategoryId);

    List<FoodItemDetailsDto> findByRestaurantIdAndFoodSubCategory(String restoId, String foodSubCategoryId);

    List<FoodItemDetailsDto> findByRestaurantIdAndFoodType(String restoId, String foodType);

    List<FoodItemDetailsDto> findByFoodCategory(String foodCategoryId);

    List<FoodItemDetailsDto> findByFoodSubCategory(String foodSubCategoryId);

    List<FoodItemDetailsDto> findByFoodType(String foodType);

    List<FoodItemDetailsDto> findByFoodName(String foodName);

    List<FoodItemDetailsDto> findByRestaurantIdAndFoodName(String restoId, String foodName);

    // ---------- RELATION MANAGEMENT ----------
    void addRestoForFood(String foodId, List<String> restoIds);

    void deleteRestoForFood(String foodId, List<String> restoIds);

    // ---------- RATING ----------
    void updateFoodRating(String foodId, double rating);
}
