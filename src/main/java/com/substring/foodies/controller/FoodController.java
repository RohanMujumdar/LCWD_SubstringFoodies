package com.substring.foodies.controller;

import com.substring.foodies.dto.FoodItemsDto;
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
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodItemsDto> addFood(@RequestBody FoodItemsDto foodItemsDto) {
        FoodItemsDto saved = foodService.addFood(foodItemsDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 🔁 Update food item
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT')")
    public ResponseEntity<FoodItemsDto> updateFood(@RequestBody FoodItemsDto foodItemsDto,
                                                   @PathVariable Long id) {
        FoodItemsDto updated = foodService.updateFood(foodItemsDto, id);
        return ResponseEntity.ok(updated);
    }

    // ❌ Delete food item
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT')")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    // 📃 Get paginated list of all food items
    @GetMapping
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
    public ResponseEntity<FoodItemsDto> getFoodById(@PathVariable Long id) {
        FoodItemsDto foodItem = foodService.getFoodById(id);
        return ResponseEntity.ok(foodItem);
    }

    // 🍽️ Get food items by restaurant ID
    @GetMapping("/byRestaurant/{restaurantId}")
    public ResponseEntity<List<FoodItemsDto>> getFoodByRestaurant(@PathVariable String restaurantId) {
        List<FoodItemsDto> foodItems = foodService.getFoodByRestaurant(restaurantId);
        return ResponseEntity.ok(foodItems);
    }
}
