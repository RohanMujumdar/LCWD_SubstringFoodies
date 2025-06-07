package com.substring.foodies.converter;

import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    private ModelMapper modelMapper;

    public User dtoToEntity(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    public List<User> dtoToEntity(List<UserDto> userDtos) {
        return userDtos.stream()
                .map(userDto -> modelMapper.map(userDto, User.class))
                .collect(Collectors.toList());
    }

    public UserDto entityToDto(User user) {

        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    // Convert List<User> to List<UserDto>
    public List<UserDto> entityToDto(List<User> users) {
        return users.stream()
                .map(user->modelMapper.map(user, UserDto.class)) // Convert each User to UserDto
                .collect(Collectors.toList());
    }


    public Restaurant restoDtoToEntity(RestaurantDto restaurantDto) {

        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);
        return restaurant;
    }

    // Convert List<RestaurantDto> to List<Restaurant>
    public List<Restaurant> restoDtoToEntity(List<RestaurantDto> restaurantDtos) {
        return restaurantDtos.stream()
                .map(restaurantDto->modelMapper.map(restaurantDto, Restaurant.class)) // Convert each RestaurantDto to Restaurant
                .collect(Collectors.toList());
    }

    // Convert Restaurant to RestaurantDto
    public RestaurantDto restoEntityToDto(Restaurant restaurant) {

        RestaurantDto restaurantDto = modelMapper.map(restaurant, RestaurantDto.class);
        return restaurantDto;
    }

    // Convert List<Restaurant> to List<RestaurantDto>
    public List<RestaurantDto> restoEntityToDto(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(restaurant->modelMapper.map(restaurant, RestaurantDto.class)) // Convert each Restaurant to RestaurantDto
                .collect(Collectors.toList());
    }
}
