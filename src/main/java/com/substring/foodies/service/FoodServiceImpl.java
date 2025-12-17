package com.substring.foodies.service;

import com.substring.foodies.dto.FoodItemsDto;
import com.substring.foodies.dto.RestaurantDto;
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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public FoodItemsDto updateFood(FoodItemsDto foodItemsDto, String id) {
        // Find the existing food item by ID
        FoodItems foodItems = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Food item not found with id: " + id)));

        // Update fields
        foodItems.setName(foodItemsDto.getName());
        foodItems.setDescription(foodItemsDto.getDescription());
        foodItems.setPrice(foodItemsDto.getPrice());
        foodItems.setAvailable(foodItemsDto.isAvailable());
        foodItems.setFoodType(foodItemsDto.getFoodType());
        foodItems.setImageUrl(foodItemsDto.getImageUrl());
        foodItems.setDiscountAmount(foodItemsDto.getDiscountAmount());

        // Optional: update restaurant if necessary
        List<RestaurantDto> restaurantDtoList = foodItemsDto.getRestaurants();
        List<String> restaurantIds = restaurantDtoList
                                        .stream()
                                        .map(resto->resto.getId())
                                        .toList();

        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        Set<String> set = restaurants
                            .stream()
                            .map(resto->resto.getId())
                            .collect(Collectors.toSet());

        List<String> missingIds = restaurantIds
                            .stream()
                            .filter(restoId->!set.contains(restoId))
                            .toList();

        if(!missingIds.isEmpty())
        {
            throw new ResourceNotFound("Restaurants not found with ids = "+missingIds);
        }

        if(foodItemsDto.getFoodCategory() != null)
        {
            FoodCategory foodCategory = foodCategoryRepository.findById(foodItemsDto.getFoodCategory().getId())
                    .orElseThrow(()->new ResourceNotFound("Food category not found with id = " + foodItemsDto.getFoodCategory().getId()));
            foodItems.setFoodCategory(foodCategory);
        }

        if(foodItemsDto.getFoodSubCategory() != null)
        {
            FoodSubCategory foodSubCategory = foodSubCategoryRepository.findById(foodItemsDto.getFoodSubCategory().getId())
                    .orElseThrow(()->new ResourceNotFound("Food sub category not found with id = " + foodItemsDto.getFoodSubCategory().getId()));
            foodItems.setFoodSubCategory(foodSubCategory);
        }

        for(Restaurant restaurant : foodItems.getRestaurants())
        {
            restaurant.getFoodItemsList().remove(foodItems);
        }

        foodItems.getRestaurants().clear();
        for(Restaurant resto: restaurants)
        {
            foodItems.getRestaurants().add(resto);
            resto.getFoodItemsList().add(foodItems);
        }

        // Save the updated entity
        FoodItems updatedFood = foodItemRepository.save(foodItems);

        // Convert back to DTO (if you have a mapper, use it; else build manually)
        return modelMapper.map(updatedFood, FoodItemsDto.class);
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
    public List<FoodItemsDto> getFoodByRestaurant(String id) {

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantsId(id);

        return foodItemsList.stream().map(foodItem -> modelMapper
                .map(foodItem, FoodItemsDto.class))
                .toList();
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
}
