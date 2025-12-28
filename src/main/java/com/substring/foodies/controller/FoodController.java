package com.substring.foodies.controller;

import com.substring.foodies.dto.FoodCategoryDto;
import com.substring.foodies.dto.FoodItemDetailsDto;
import com.substring.foodies.dto.FoodItemRequestDto;
import com.substring.foodies.dto.FoodItemsMenuDto;
import com.substring.foodies.dto.enums.FoodType;
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

    // ---------------- CREATE ----------------
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemsMenuDto> addFood(
            @RequestBody FoodItemRequestDto dto) {

        return new ResponseEntity<>(
                foodService.addFood(dto),
                HttpStatus.CREATED
        );
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemsMenuDto> updateFood(
            @RequestBody FoodItemRequestDto dto,
            @PathVariable String id) {

        return ResponseEntity.ok(
                foodService.updateFood(dto, id)
        );
    }

    // ---------------- PATCH (SCALARS ONLY) ----------------
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemsMenuDto> patchFood(
            @PathVariable String id,
            @RequestBody FoodItemsMenuDto patchDto) {

        return ResponseEntity.ok(
                foodService.patchFood(id, patchDto)
        );
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<Void> deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- ADMIN / DETAILS ----------------
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<Page<FoodItemDetailsDto>> getAllFoodItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(
                foodService.getAllFoodItems(pageable)
        );
    }

    @GetMapping("/{foodId}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<FoodItemDetailsDto> getFoodById(
            @PathVariable String foodId) {

        return ResponseEntity.ok(
                foodService.getFoodById(foodId)
        );
    }

    // ---------------- MENU (USER) ----------------
    @GetMapping("/restaurant/{restaurantId}/menu")
    public ResponseEntity<List<FoodCategoryDto>> getMenuByRestaurant(
            @PathVariable String restaurantId) {

        return ResponseEntity.ok(
                foodService.getFoodByRestaurant(restaurantId)
        );
    }

    // ---------------- FILTERS ----------------
    @GetMapping("/restaurant/{restaurantId}/category/{categoryId}")
    public ResponseEntity<List<FoodItemDetailsDto>> byCategory(
            @PathVariable String restaurantId,
            @PathVariable String categoryId) {

        return ResponseEntity.ok(
                foodService.getFoodByRestaurantAndCategory(restaurantId, categoryId)
        );
    }

    @GetMapping("/restaurant/{restaurantId}/subcategory/{subCategoryId}")
    public ResponseEntity<List<FoodItemDetailsDto>> bySubCategory(
            @PathVariable String restaurantId,
            @PathVariable String subCategoryId) {

        return ResponseEntity.ok(
                foodService.getFoodByRestaurantAndCategory(restaurantId, subCategoryId)
        );
    }

    @GetMapping("/restaurant/{restaurantId}/type/{type}")
    public ResponseEntity<List<FoodItemDetailsDto>> byType(
            @PathVariable String restaurantId,
            @PathVariable FoodType type) {

        return ResponseEntity.ok(
                foodService.getFoodByRestaurantAndFoodType(restaurantId, type)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<FoodItemDetailsDto>> byFoodCategory(
            @PathVariable String categoryId) {

        return ResponseEntity.ok(
                foodService.getFoodByCategory(categoryId)
        );
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<List<FoodItemDetailsDto>> byFoodSubCategory(
            @PathVariable String subCategoryId) {

        return ResponseEntity.ok(
                foodService.getFoodBySubCategory(subCategoryId)
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FoodItemDetailsDto>> byFoodType(
            @PathVariable FoodType type) {

        return ResponseEntity.ok(
                foodService.getFoodByFoodType(type)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodItemDetailsDto>> byName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                foodService.searchFoodByName(name)
        );
    }

    @GetMapping("/restaurant/{restaurantId}/search")
    public ResponseEntity<List<FoodItemDetailsDto>> byRestaurantAndName(
            @PathVariable String restaurantId,
            @RequestParam String name) {

        return ResponseEntity.ok(
                foodService.searchFoodByRestaurantAndName(restaurantId, name)
        );
    }

    // ---------------- RELATION MANAGEMENT ----------------
    @PostMapping("/{foodId}/restaurants")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<Void> addRestoForFood(
            @PathVariable String foodId,
            @RequestBody List<String> restoIds) {

        foodService.addRestoForFood(foodId, restoIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{foodId}/restaurants")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT_ADMIN')")
    public ResponseEntity<Void> deleteRestoForFood(
            @PathVariable String foodId,
            @RequestBody List<String> restoIds) {

        foodService.deleteRestoForFood(foodId, restoIds);
        return ResponseEntity.noContent().build();
    }

    // ---------------- RATING ----------------
    @PatchMapping("/{foodId}/rating")
    public ResponseEntity<Void> updateFoodRating(
            @PathVariable String foodId,
            @RequestParam double rating) {

        foodService.updateFoodRating(foodId, rating);
        return ResponseEntity.noContent().build();
    }
}
