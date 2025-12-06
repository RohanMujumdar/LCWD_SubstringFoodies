package com.substring.foodies.service;

import com.substring.foodies.dto.FoodItemsDto;
import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.entity.FoodItems;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.FoodItemRepository;
import com.substring.foodies.repository.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FoodServiceImpl implements FoodService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @Override
    public FoodItemsDto addFood(FoodItemsDto foodItemsDto) {

        Restaurant restaurant = restaurantRepository.findById(foodItemsDto.getRestaurantId()).orElseThrow(()->new ResourceNotFound("No Restaurant Found"));

        foodItemRepository.save(modelMapper.map(foodItemsDto, FoodItems.class));
        return foodItemsDto;
    }


    @Override
    public FoodItemsDto updateFood(FoodItemsDto foodItemsDto, Long id) {
        // Find the existing food item by ID
        FoodItems foodItems = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Food item not found with id: " + id));

        // Update fields
        foodItems.setName(foodItemsDto.getName());
        foodItems.setDescription(foodItemsDto.getDescription());
        foodItems.setPrice(foodItemsDto.getPrice());
        foodItems.setAvailable(foodItemsDto.isAvailable());
        foodItems.setFoodType(foodItemsDto.getFoodType());
        foodItems.setImageUrl(foodItemsDto.getImageUrl());
        foodItems.setDiscountAmount(foodItemsDto.getDiscountAmount());

        // Optional: update restaurant if necessary
        if (foodItemsDto.getRestaurantId() != null) {
            Restaurant restaurant = restaurantRepository.findById(foodItemsDto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFound("Restaurant not found with id: " + foodItemsDto.getRestaurantId()));
            foodItems.setRestaurant(restaurant);
        }

        // Save the updated entity
        FoodItems updatedFood = foodItemRepository.save(foodItems);

        // Convert back to DTO (if you have a mapper, use it; else build manually)
        return modelMapper.map(updatedFood, FoodItemsDto.class);
    }


    @Override
    public void deleteFood(Long id) {

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

        List<FoodItems> foodItemsList = foodItemRepository.findByRestaurantId(id);

        return foodItemsList.stream().map(foodItem -> modelMapper
                .map(foodItem, FoodItemsDto.class))
                .toList();
    }

    @Override
    public FoodItemsDto getFoodById(Long foodId) {

        FoodItems foodItems = foodItemRepository.findById(foodId).orElseThrow(() -> new ResourceNotFound("Food Item not found"));
        return modelMapper.map(foodItems, FoodItemsDto.class);

    }
}
