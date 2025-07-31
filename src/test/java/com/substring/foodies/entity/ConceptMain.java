package com.substring.foodies.entity;

import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
