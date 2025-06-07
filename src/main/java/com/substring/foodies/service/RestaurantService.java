package com.substring.foodies.service;

import com.substring.foodies.dto.FileData;
import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface RestaurantService {

    Restaurant savedRestaurant(RestaurantDto restaurantDto);
    Page<RestaurantDto> getAllRestaurants(Pageable pageable);
    RestaurantDto updateSavedRestaurant(RestaurantDto restaurantDto, String id);
    List<RestaurantDto> findRestaurantByName(String name);
    RestaurantDto getRestaurantById (String id);
    void deleteRestaurant(String id);

    Page<RestaurantDto> getAllOpenRestaurants(Pageable pageable);

    List<RestaurantDto> findByNameLikeRestaurants(String pattern);

    List<RestaurantDto> findCurrentOpenRestaurants();

    RestaurantDto uploadBanner(MultipartFile file, String id) throws IOException;

    void deleteBanner(String path);
}
