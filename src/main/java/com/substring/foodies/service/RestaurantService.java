package com.substring.foodies.service;

import com.substring.foodies.dto.FileData;
import com.substring.foodies.dto.OrderDto;
import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.dto.enums.OrderStatus;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
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

    List<RestaurantDto> findByNameLikeRestaurants(String pattern);

    List<RestaurantDto> findCurrentOpenAndActiveRestaurants(boolean isActive, boolean isOpen);

    RestaurantDto uploadBanner(MultipartFile file, String id) throws IOException;

    void deleteBanner(String path);

    List<RestaurantDto> getByOwner(String ownerId);
    List<RestaurantDto> findRestaurantByName(String name);
    List<RestaurantDto> findRestaurantByIsActive(Boolean isActive);


}
