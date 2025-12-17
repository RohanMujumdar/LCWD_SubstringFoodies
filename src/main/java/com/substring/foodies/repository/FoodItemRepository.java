package com.substring.foodies.repository;

import com.substring.foodies.entity.FoodItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FoodItemRepository extends JpaRepository<FoodItems, String> {

    List<FoodItems> findByRestaurantsId(String restoId);
    List<FoodItems> findByRestaurantsIdAndFoodCategoryId(String restoId, String foodCategoryId);
    List<FoodItems> findByRestaurantsIdAndFoodSubCategoryId(String restoId, String foodSubCategoryId);
    List<FoodItems> findByRestaurantsIdAndFoodType(String restoId, String foodType);
    List<FoodItems> findByFoodCategoryId(String foodCategoryId);
    List<FoodItems> findByFoodSubCategoryId(String foodSubCategoryId);
    List<FoodItems> findByFoodType(String foodType);
    List<FoodItems> findByName(String foodName);
    List<FoodItems> findByRestaurantsIdAndName(String restoId, String foodName);
}
