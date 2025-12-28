package com.substring.foodies.service;

import com.substring.foodies.dto.*;
import com.substring.foodies.dto.enums.FoodType;
import com.substring.foodies.entity.FoodCategory;
import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.FoodSubCategory;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.exception.FoodCategoryException;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.FoodCategoryRepository;
import com.substring.foodies.repository.FoodItemRepository;
import com.substring.foodies.repository.FoodSubCategoryRepository;
import com.substring.foodies.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private FoodSubCategoryRepository foodSubCategoryRepository;

    // ===================== HELPERS =====================

    private void validateRestaurant(String restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFound("Restaurant not found with id = " + restaurantId);
        }
    }

    // ===================== CREATE =====================

    @Override
    public FoodItemsMenuDto addFood(FoodItemRequestDto dto) {

        FoodCategory category = foodCategoryRepository
                .findById(dto.getFoodCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFound("Food category not found with id = " + dto.getFoodCategoryId()));

        FoodSubCategory subCategory = foodSubCategoryRepository
                .findById(dto.getFoodSubCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFound("Food subcategory not found with id = " + dto.getFoodSubCategoryId()));

        if (!subCategory.getFoodCategory().getId().equals(category.getId())) {
            throw new FoodCategoryException("SubCategory does not belong to category");
        }

        FoodItems foodItem = modelMapper.map(dto, FoodItems.class);

        List<Restaurant> restaurants =
                restaurantRepository.findAllById(dto.getRestaurantIds());

        if (restaurants.size() != dto.getRestaurantIds().size()) {
            throw new ResourceNotFound("One or more restaurants not found");
        }

        restaurants.forEach(resto -> {
            resto.getFoodItemsList().add(foodItem);
            foodItem.getRestaurants().add(resto);
        });

        foodItem.setFoodCategory(category);
        foodItem.setFoodSubCategory(subCategory);

        FoodItems saved = foodItemRepository.save(foodItem);
        return modelMapper.map(saved, FoodItemsMenuDto.class);
    }

    // ===================== UPDATE =====================

    @Override
    public FoodItemsMenuDto updateFood(FoodItemRequestDto dto, String foodId) {

        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + foodId));

        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setPrice(dto.getPrice());
        food.setIsAvailable(dto.getIsAvailable());
        food.setFoodType(dto.getFoodType());
        food.setImageUrl(dto.getImageUrl());
        food.setDiscountAmount(dto.getDiscountAmount());

        FoodCategory category = foodCategoryRepository
                .findById(dto.getFoodCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFound("Food category not found"));

        FoodSubCategory subCategory = foodSubCategoryRepository
                .findById(dto.getFoodSubCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFound("Food subcategory not found"));

        if (!subCategory.getFoodCategory().getId().equals(category.getId())) {
            throw new FoodCategoryException("SubCategory does not belong to category");
        }

        food.setFoodCategory(category);
        food.setFoodSubCategory(subCategory);

        FoodItems updated = foodItemRepository.save(food);
        return modelMapper.map(updated, FoodItemsMenuDto.class);
    }

    // ===================== PATCH =====================

    @Override
    public FoodItemsMenuDto patchFood(String foodId, FoodItemsMenuDto patchDto) {

        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food not found with id = " + foodId));

        if (patchDto.getName() != null) food.setName(patchDto.getName());
        if (patchDto.getDescription() != null) food.setDescription(patchDto.getDescription());
        if (patchDto.getPrice() > 0) food.setPrice(patchDto.getPrice());
        if (patchDto.getIsAvailable() != null) food.setIsAvailable(patchDto.getIsAvailable());
        if (patchDto.getFoodType() != null) food.setFoodType(patchDto.getFoodType());
        if (patchDto.getImageUrl() != null) food.setImageUrl(patchDto.getImageUrl());
        if (patchDto.getDiscountAmount() >= 0) food.setDiscountAmount(patchDto.getDiscountAmount());

        FoodItems updated = foodItemRepository.save(food);
        return modelMapper.map(updated, FoodItemsMenuDto.class);
    }

    // ===================== DELETE =====================

    @Override
    public void deleteFood(String foodId) {
        if (!foodItemRepository.existsById(foodId)) {
            throw new ResourceNotFound("Food item not found with id = " + foodId);
        }
        foodItemRepository.deleteById(foodId);
    }

    // ===================== ADMIN =====================

    @Override
    public Page<FoodItemDetailsDto> getAllFoodItems(Pageable pageable) {
        return foodItemRepository
                .findAll(pageable)
                .map(food -> modelMapper.map(food, FoodItemDetailsDto.class));
    }

    @Override
    public FoodItemDetailsDto getFoodById(String foodId) {
        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + foodId));
        return modelMapper.map(food, FoodItemDetailsDto.class);
    }

    // ===================== MENU =====================

    @Override
    public List<FoodCategoryDto> getFoodByRestaurant(String restaurantId) {

        validateRestaurant(restaurantId);

        List<FoodItems> foodItems = foodItemRepository.findMenuByRestaurant(restaurantId);

        Map<String, FoodCategoryDto> categoryMap = new LinkedHashMap<>();

        for (FoodItems food : foodItems) {

            FoodCategory category = food.getFoodCategory();
            FoodSubCategory subCategory = food.getFoodSubCategory();

            if (category == null || subCategory == null) continue;

            FoodCategoryDto categoryDto = categoryMap.computeIfAbsent(
                    category.getId(),
                    id -> new FoodCategoryDto(
                            category.getId(),
                            category.getName(),
                            category.getDescription(),
                            new ArrayList<>()
                    )
            );

            FoodSubCategoryResponseDto subCategoryDto =
                    categoryDto.getSubCategories()
                            .stream()
                            .filter(sc -> sc.getId().equals(subCategory.getId()))
                            .findFirst()
                            .orElseGet(() -> {
                                FoodSubCategoryResponseDto scDto =
                                        new FoodSubCategoryResponseDto(
                                                subCategory.getId(),
                                                subCategory.getName(),
                                                new ArrayList<>()
                                        );
                                categoryDto.getSubCategories().add(scDto);
                                return scDto;
                            });

            subCategoryDto.getFoodItems()
                    .add(modelMapper.map(food, FoodItemsMenuDto.class));
        }

        categoryMap.values().forEach(cat ->
                cat.getSubCategories().forEach(sub ->
                        sub.getFoodItems()
                                .sort(Comparator.comparing(FoodItemsMenuDto::getRating).reversed())
                )
        );

        return new ArrayList<>(categoryMap.values());
    }

    // ===================== FILTERS =====================

    @Override
    public List<FoodItemDetailsDto> getFoodByRestaurantAndCategory(
            String restaurantId, String foodCategoryId) {

        validateRestaurant(restaurantId);

        return foodItemRepository
                .findByRestaurantsIdAndFoodCategoryIdOrderByRatingDesc(
                        restaurantId, foodCategoryId)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> getFoodByRestaurantAndSubCategory(
            String restaurantId, String foodSubCategoryId) {

        validateRestaurant(restaurantId);

        return foodItemRepository
                .findByRestaurantsIdAndFoodSubCategoryIdOrderByRatingDesc(
                        restaurantId, foodSubCategoryId)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> getFoodByRestaurantAndFoodType(
            String restaurantId, FoodType foodType) {

        validateRestaurant(restaurantId);

        return foodItemRepository
                .findByRestaurantsIdAndFoodTypeOrderByRatingDesc(
                        restaurantId, foodType)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> getFoodByCategory(String foodCategoryId) {
        return foodItemRepository
                .findByFoodCategoryIdOrderByRatingDesc(foodCategoryId)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> getFoodBySubCategory(String foodSubCategoryId) {
        return foodItemRepository
                .findByFoodSubCategoryIdOrderByRatingDesc(foodSubCategoryId)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> getFoodByFoodType(FoodType foodType) {
        return foodItemRepository
                .findByFoodTypeOrderByRatingDesc(foodType)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> searchFoodByName(String foodName) {
        return foodItemRepository
                .findByNameIgnoreCaseContainingOrderByRatingDesc(foodName)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    @Override
    public List<FoodItemDetailsDto> searchFoodByRestaurantAndName(
            String restaurantId, String foodName) {

        validateRestaurant(restaurantId);

        return foodItemRepository
                .findByRestaurantsIdAndNameIgnoreCaseContainingOrderByRatingDesc(
                        restaurantId, foodName)
                .stream()
                .map(f -> modelMapper.map(f, FoodItemDetailsDto.class))
                .toList();
    }

    // ===================== RELATION MANAGEMENT =====================

    @Override
    @Transactional
    public void addRestoForFood(String foodId, List<String> restaurantIds) {

        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + foodId));

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);

        if (restaurants.size() != restaurantIds.size()) {
            throw new ResourceNotFound("One or more restaurants not found");
        }

        restaurants.forEach(resto -> resto.getFoodItemsList().add(food));
        restaurantRepository.saveAll(restaurants);
    }

    @Override
    @Transactional
    public void deleteRestoForFood(String foodId, List<String> restaurantIds) {

        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + foodId));

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);

        if (restaurants.size() != restaurantIds.size()) {
            throw new ResourceNotFound("One or more restaurants not found");
        }

        restaurants.forEach(resto -> resto.getFoodItemsList().remove(food));
        restaurantRepository.saveAll(restaurants);
    }

    // ===================== RATING =====================

    @Override
    @Transactional
    public void updateFoodRating(String foodId, double rating) {

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        FoodItems food = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + foodId));

        food.setRating(rating);
        foodItemRepository.save(food);

        updateRestaurantRating(food.getRestaurants());
    }

    private void updateRestaurantRating(Set<Restaurant> restaurants) {
        for (Restaurant r : restaurants) {
            double avg = foodItemRepository.avgRatingByRestaurant(r.getId());
            r.setRating(avg);
            restaurantRepository.save(r);
        }
    }
}
