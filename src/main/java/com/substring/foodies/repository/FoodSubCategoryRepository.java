package com.substring.foodies.repository;

import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.entity.FoodSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodSubCategoryRepository extends JpaRepository<FoodSubCategory, String> {

    boolean existsByNameIgnoreCaseAndFoodCategoryId(String name, String foodCategoryId);
}
