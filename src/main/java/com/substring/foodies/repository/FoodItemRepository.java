package com.substring.foodies.repository;

import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.dto.enums.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItems, String> {

    // ===================== MENU =====================

    @Query("""
        select f
        from FoodItems f
        join f.restaurants r
        join f.foodCategory c
        join f.foodSubCategory sc
        where r.id = :restaurantId
          and f.isAvailable = true
        order by 
            c.displayOrder asc,
            sc.displayOrder asc,
            f.rating desc
    """)
    List<FoodItems> findMenuByRestaurant(@Param("restaurantId") String restaurantId);


    // ===================== RESTAURANT-SCOPED FILTERS =====================

    List<FoodItems> findByRestaurantsIdAndFoodCategoryIdOrderByRatingDesc(
            String restaurantId,
            String foodCategoryId
    );

    List<FoodItems> findByRestaurantsIdAndFoodSubCategoryIdOrderByRatingDesc(
            String restaurantId,
            String foodSubCategoryId
    );

    List<FoodItems> findByRestaurantsIdAndFoodTypeOrderByRatingDesc(
            String restaurantId,
            FoodType foodType
    );

    List<FoodItems> findByRestaurantsIdAndNameIgnoreCaseContainingOrderByRatingDesc(
            String restaurantId,
            String name
    );


    // ===================== GLOBAL FILTERS =====================

    List<FoodItems> findByFoodCategoryIdOrderByRatingDesc(String foodCategoryId);

    List<FoodItems> findByFoodSubCategoryIdOrderByRatingDesc(String foodSubCategoryId);

    List<FoodItems> findByFoodTypeOrderByRatingDesc(FoodType foodType);

    List<FoodItems> findByNameIgnoreCaseContainingOrderByRatingDesc(String name);


    // ===================== RATING =====================

    @Query("""
        select COALESCE(AVG(f.rating), 0)
        from FoodItems f
        join f.restaurants r
        where r.id = :restaurantId
    """)
    double avgRatingByRestaurant(@Param("restaurantId") String restaurantId);
}
