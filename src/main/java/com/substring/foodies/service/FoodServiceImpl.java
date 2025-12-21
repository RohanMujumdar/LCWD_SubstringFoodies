package com.substring.foodies.service;

import com.substring.foodies.dto.FoodCategoryDto;
import com.substring.foodies.dto.FoodItemsDto;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FoodServiceImpl implements FoodService{

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


    @Override
    public FoodItemsDto addFood(FoodItemsDto dto) {

            FoodCategory category = foodCategoryRepository
                    .findById(dto.getFoodCategory().getId())
                    .orElseThrow(() ->
                            new ResourceNotFound(
                                    "Food category not found with id = " + dto.getFoodCategory().getId())
                    );


            // 3️⃣ Fetch SubCategory from DB
            FoodSubCategory subCategory = foodSubCategoryRepository
                    .findById(dto.getFoodSubCategory().getId())
                    .orElseThrow(() ->
                            new ResourceNotFound(
                                    "Food subcategory not found with id = " + dto.getFoodSubCategory().getId()
                            )
                    );

            // 4️⃣ 🔥 VALIDATION CHECK
            if (!subCategory.getFoodCategory().getId().equals(category.getId())) {
                throw new FoodCategoryException(
                        "SubCategory '" + subCategory.getName() +
                                "' does not belong to Category '" + category.getName() + "'"
                );
            }

            FoodItems foodItem = modelMapper.map(dto, FoodItems.class);
            List<String> restaurantIds = dto.getRestaurants()
                                            .stream()
                                            .map(resto -> resto.getId())
                                            .toList();

            List<Restaurant> restaurantList = restaurantRepository.findAllById(restaurantIds);
            Set<String> set = restaurantList.stream().map(restaurant -> restaurant.getId()).collect(Collectors.toSet());

            List<String> missingIds = restaurantIds.stream()
                    .filter(id -> !set.contains(id))
                    .toList();

            if(!missingIds.isEmpty())
            {
                throw new ResourceNotFound("Restaurants not found with ids = "+missingIds);
            }

            for(Restaurant resto : restaurantList)
            {
                 resto.getFoodItemsList().add(foodItem);
                 foodItem.getRestaurants().add(resto);
            }

            // 5️⃣ Map & assign MANAGED entities
            foodItem.setFoodCategory(category);
            foodItem.setFoodSubCategory(subCategory);

            foodItemRepository.save(foodItem);
            return modelMapper.map(foodItem, FoodItemsDto.class);
    }


    @Override
    public FoodItemsDto updateFood(FoodItemsDto dto, String id) {

        FoodItems food = foodItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Food item not found with id = " + id)
                );

        // FULL UPDATE (PUT semantics)
        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setPrice(dto.getPrice());
        food.setIsAvailable(dto.getIsAvailable());
        food.setFoodType(dto.getFoodType());
        food.setImageUrl(dto.getImageUrl());
        food.setDiscountAmount(dto.getDiscountAmount());

        // Category
        if (dto.getFoodCategory() != null) {
            FoodCategory category = foodCategoryRepository
                    .findById(dto.getFoodCategory().getId())
                    .orElseThrow(() ->
                            new ResourceNotFound(
                                    "Food category not found with id = " +
                                            dto.getFoodCategory().getId()
                            )
                    );
            food.setFoodCategory(category);
        }

        // Sub-category
        if (dto.getFoodSubCategory() != null) {
            FoodSubCategory subCategory = foodSubCategoryRepository
                    .findById(dto.getFoodSubCategory().getId())
                    .orElseThrow(() ->
                            new ResourceNotFound(
                                    "Food sub category not found with id = " +
                                            dto.getFoodSubCategory().getId()
                            )
                    );
            food.setFoodSubCategory(subCategory);
        }

        // ❌ DO NOT TOUCH RESTAURANTS HERE

        FoodItems updatedFood = foodItemRepository.save(food);
        return modelMapper.map(updatedFood, FoodItemsDto.class);
    }


    @Override
    public FoodItemsDto patchFood(String id, FoodItemsDto patchDto) {
        FoodItems food = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Food not found with id = " + id));

        // ---- Scalar fields only ----
        if (patchDto.getName() != null) {
            food.setName(patchDto.getName());
        }

        if (patchDto.getDescription() != null) {
            food.setDescription(patchDto.getDescription());
        }

        if (patchDto.getPrice() > 0) {
            food.setPrice(patchDto.getPrice());
        }

        // boolean needs special handling
        if(patchDto.getIsAvailable() != null)
        {
            food.setIsAvailable(patchDto.getIsAvailable());
        }

        if (patchDto.getFoodType() != null) {
            food.setFoodType(patchDto.getFoodType());
        }

        if (patchDto.getFoodCategory() != null) {
            food.setFoodCategory(patchDto.getFoodCategory());
        }

        if (patchDto.getFoodSubCategory() != null) {
            food.setFoodSubCategory(patchDto.getFoodSubCategory());
        }

        if (patchDto.getImageUrl() != null) {
            food.setImageUrl(patchDto.getImageUrl());
        }

        if (patchDto.getDiscountAmount() >= 0) {
            food.setDiscountAmount(patchDto.getDiscountAmount());
        }

        // ---- DO NOT PATCH RELATIONSHIPS HERE ----
        // restaurants list is intentionally ignored

        FoodItems updated = foodItemRepository.save(food);
        return modelMapper.map(updated, FoodItemsDto.class);
    }


    @Override
    public void deleteFood(String id) {

        if(!foodItemRepository.existsById(id))
        {
            throw new ResourceNotFound("Food Item not found");
        }
        foodItemRepository.deleteById(id);
    }


    @Override
    public Page<FoodItemsDto> getAllFoodItems(Pageable pageable) {

        Page<FoodItems> foodItemsList = foodItemRepository.findAll(pageable);
        return foodItemsList
                .map(food -> modelMapper
                .map(food, FoodItemsDto.class));
    }

    @Override
    public List<FoodCategoryDto> getFoodByRestaurant(String restoId) {

        Restaurant restaurant = restaurantRepository
                                    .findById(restoId)
                                    .orElseThrow(()->new ResourceNotFound("Food item not found with id = "+restoId));

        Set<FoodItems> foodItemsList = restaurant.getFoodItemsList();

        List<FoodCategoryDto> foodCategoryDtoList = foodItemsList
                                                        .stream()
                                                        .map(food->food.getFoodCategory())
                                                        .filter(Objects::nonNull)
                                                        .distinct()
                                                        .map(category -> modelMapper.map(category, FoodCategoryDto.class))
                                                        .toList();

        return foodCategoryDtoList;
    }

    @Override
    public List<FoodItemsDto> findByRestaurantIdAndFoodCategory(String restoId, String foodCategoryId) {

        restaurantRepository.findById(restoId).orElseThrow(()->new ResourceNotFound(String.format("Restaurant not found with id: " + restoId)));

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantsIdAndFoodCategoryId(restoId, foodCategoryId);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByRestaurantIdAndFoodSubCategory(String restoId, String foodSubCategoryId) {

        restaurantRepository.findById(restoId).orElseThrow(()->new ResourceNotFound(String.format("Restaurant not found with id: " + restoId)));

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantsIdAndFoodSubCategoryId(restoId, foodSubCategoryId);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByRestaurantIdAndFoodType(String restoId, String foodType) {

        restaurantRepository.findById(restoId).orElseThrow(()->new ResourceNotFound(String.format("Restaurant not found with id: " + restoId)));

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantsIdAndFoodType(restoId, foodType);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByFoodCategory(String foodCategoryId) {
        List<FoodItems> foodItemsList = foodItemRepository.findByFoodCategoryId(foodCategoryId);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByFoodSubCategory(String foodSubCategoryId) {
        List<FoodItems> foodItemsList = foodItemRepository.findByFoodSubCategoryId(foodSubCategoryId);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByFoodType(String foodType) {
        List<FoodItems> foodItemsList = foodItemRepository.findByFoodType(foodType);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();

    }

    @Override
    public FoodItemsDto getFoodById(String foodId) {

        FoodItems foodItems = foodItemRepository.findById(foodId).orElseThrow(() -> new ResourceNotFound("Food Item not found with id = " + foodId));
        return modelMapper.map(foodItems, FoodItemsDto.class);
    }

    @Override
    public List<FoodItemsDto> findByFoodName(String foodName) {
        List<FoodItems> foodItemsList = foodItemRepository.findByName(foodName);
        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();
    }

    @Override
    public List<FoodItemsDto> findByRestaurantIdAndFoodName(String restoId, String foodName) {

        restaurantRepository.findById(restoId).orElseThrow(()->new ResourceNotFound(String.format("Restaurant not found with id = " + restoId)));

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantsIdAndName(restoId, foodName);

        return foodItemsList.stream().map(foodItems -> modelMapper.map(foodItems, FoodItemsDto.class)).toList();

    }

    @Override
    @Transactional
    public void addRestoForFood(String foodId, List<String> restoIds) {

        FoodItems foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food Item not found with id = " + foodId)
                );

        List<Restaurant> restaurants = restaurantRepository.findAllById(restoIds);

        if (restaurants.size() != restoIds.size()) {
            Set<String> foundIds = restaurants.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toSet());

            List<String> missingIds = restoIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResourceNotFound("Restaurants not found with ids: " + missingIds);
        }

        for (Restaurant resto : restaurants) {
            resto.getFoodItemsList().add(foodItem);
        }

        // save once (owning side)
        restaurantRepository.saveAll(restaurants);
    }


    @Override
    @Transactional
    public void deleteRestoForFood(String foodId, List<String> restoIds) {

        FoodItems foodItem = foodItemRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFound("Food Item not found with id = " + foodId)
                );

        List<Restaurant> restaurants = restaurantRepository.findAllById(restoIds);

        if (restaurants.size() != restoIds.size()) {
            Set<String> foundIds = restaurants.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toSet());

            List<String> missingIds = restoIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResourceNotFound("Restaurants not found with ids: " + missingIds);
        }

        for (Restaurant resto : restaurants) {
            resto.getFoodItemsList().remove(foodItem);
        }

        restaurantRepository.saveAll(restaurants);
    }

    @Override
    @Transactional
    public void updateFoodRating(String foodId, double rating) {

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        FoodItems foodItem = foodItemRepository
                                .findById(foodId)
                                .orElseThrow(()->new ResourceNotFound("Food Item not found with id = " + foodId));

        foodItem.setRating(rating);
        foodItemRepository.save(foodItem);
        updateRestaurantRating(foodItem.getRestaurants());
    }

    private void updateRestaurantRating(Set<Restaurant> restaurants) {
        for (Restaurant r : restaurants) {
            double avg = foodItemRepository.avgRatingByRestaurant(r.getId());
            r.setRating(avg);
            restaurantRepository.save(r);
        }
    }

}
