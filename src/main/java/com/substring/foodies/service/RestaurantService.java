package com.substring.foodies.service;

import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


public interface RestaurantService {

    RestaurantDto addRestaurant(RestaurantDto restaurantDto);

    Page<RestaurantDto> getAllRestaurants(Pageable pageable);

    RestaurantDto updateSavedRestaurant(RestaurantDto restaurantDto, String id);

    RestaurantDto getRestaurantById (String id);

    void deleteRestaurant(String id);

    Page<RestaurantDto> getAllOpenRestaurants(Pageable pageable);

    List<RestaurantDto> findByNameContainingIgnoreCase(String pattern);

    RestaurantDto uploadBanner(MultipartFile file, String id) throws IOException;

    void deleteBanner(String path);

    List<RestaurantDto> getByOwner(String ownerId);

    Page<RestaurantDto> findByFoodItemsList_Id(String foodId, Pageable pageable);

}
