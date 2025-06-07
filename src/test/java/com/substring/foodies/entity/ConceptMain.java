package com.substring.foodies.entity;

import com.substring.foodies.dto.Role;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ConceptMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @Test
    public void testOrphanRemoval()
    {
//        User u1 = userRepository.findById("user123").orElseThrow(()-> new RuntimeException("No User Found"));
//        List<Restaurant> restaurantList = u1.getRestaurantList();
//
//        restaurantList.remove(4);
//
//        restaurantList.forEach(rest -> {
//
//        });
//
//        userRepository.save(u1);
    }
}
