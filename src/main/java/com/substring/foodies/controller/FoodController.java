package com.substring.foodies.controller;

import com.substring.foodies.dto.FoodCategoryDto;
import com.substring.foodies.dto.FoodItemsDto;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    // ➕ Add food item
    @PostMapping("/")
    public ResponseEntity<FoodItemsDto> addFood(@RequestBody FoodItemsDto foodItemsDto) {
        FoodItemsDto saved = foodService.addFood(foodItemsDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 🔁 Update food item
    @PutMapping("/{id}")
    public ResponseEntity<FoodItemsDto> updateFood(@RequestBody FoodItemsDto foodItemsDto,
                                                   @PathVariable String id) {
        FoodItemsDto updated = foodService.updateFood(foodItemsDto, id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemsDto> patchFood(@PathVariable String id, @RequestBody FoodItemsDto patchDto)
    {
        FoodItemsDto updatedFood = foodService.patchFood(id, patchDto);
        return ResponseEntity.ok(updatedFood);
    }

    // ➕ Add food to multiple restaurants
    @PostMapping("/{foodId}/restaurants")
    public ResponseEntity<?> addRestoForFood(
            @PathVariable String foodId,
            @RequestBody List<String> restoIds
    ) {
        foodService.addRestoForFood(foodId, restoIds);
        return new ResponseEntity<>("Restaurants added successfully", HttpStatus.OK);
    }

    // ❌ Remove food from multiple restaurants
    @DeleteMapping("/{foodId}/restaurants")
    public ResponseEntity<?> deleteRestoForFood(
            @PathVariable String foodId,
            @RequestBody List<String> restoIds
    ) {
        foodService.deleteRestoForFood(foodId, restoIds);
        return new ResponseEntity<>("Restaurants deleted successfully", HttpStatus.OK);

    }
    // ❌ Delete food item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    // 📃 Get paginated list of all food items
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<Page<FoodItemsDto>> getAllFoodItems(@RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                              @RequestParam(value="size", required = false, defaultValue = "6") int size,
                                                              @RequestParam(value="sortBy", required = false, defaultValue = "id") String sortBy,
                                                              @RequestParam(value="sortDir", required = false, defaultValue = "asc") String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page, size, sort);

        Page<FoodItemsDto> foodItems = foodService.getAllFoodItems(pageable);
        return ResponseEntity.ok(foodItems);
    }

    // 🔍 Get food item by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemsDto> getFoodById(@PathVariable String id) {
        FoodItemsDto foodItem = foodService.getFoodById(id);
        return ResponseEntity.ok(foodItem);
    }

    // 🍽️ Get food items by restaurant ID
    @GetMapping("/menuByRestaurant/{restaurantId}")
    public ResponseEntity<List<FoodCategoryDto>> getFoodByRestaurant(@PathVariable String restaurantId) {
        List<FoodCategoryDto> foodCategoryDtoList = foodService.getFoodByRestaurant(restaurantId);
        return ResponseEntity.ok(foodCategoryDtoList);
    }

    @GetMapping("/menuByRestaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByRestaurant_Category(@PathVariable String restaurantId, @PathVariable String category) {
        List<FoodItemsDto> foodItems = foodService.findByRestaurantIdAndFoodCategory(restaurantId, category);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/menuByRestaurant/{restaurantId}/subcategory/{subcategory}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByRestaurant_subCategory(@PathVariable String restaurantId, @PathVariable String subcategory) {
        List<FoodItemsDto> foodItems = foodService.findByRestaurantIdAndFoodSubCategory(restaurantId, subcategory);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/menuByRestaurant/{restaurantId}/type/{type}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByRestaurant_type(@PathVariable String restaurantId, @PathVariable String type) {
        List<FoodItemsDto> foodItems = foodService.findByRestaurantIdAndFoodType(restaurantId, type);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/menuByCategory/{category}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByCategory(@PathVariable String category) {
        List<FoodItemsDto> foodItems = foodService.findByFoodCategory(category);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/menuBySubCategory/{subCategory}")
    public ResponseEntity<List<FoodItemsDto>> getFoodBySubCategory(@PathVariable String subCategory) {
        List<FoodItemsDto> foodItems = foodService.findByFoodSubCategory(subCategory);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/menuByType/{type}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByType(@PathVariable String type) {
        List<FoodItemsDto> foodItems = foodService.findByFoodType(type);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByName(@PathVariable String name) {
        List<FoodItemsDto> foodItems = foodService.findByFoodName(name);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/restaurant/{restaurantId}/{name}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByResaturantAndFoodName(@PathVariable String restaurantId, @PathVariable String name) {
        List<FoodItemsDto> foodItems = foodService.findByRestaurantIdAndFoodName(restaurantId, name);
        return ResponseEntity.ok(foodItems);
    }

    @PatchMapping("/{foodId}/rating")
    public ResponseEntity<Void> updateFoodRating(
            @PathVariable String foodId,
            @RequestParam double rating
    ) {
        foodService.updateFoodRating(foodId, rating);
        return ResponseEntity.noContent().build();
    }
}
