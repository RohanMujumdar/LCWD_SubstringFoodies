package com.substring.foodies.repository;

import com.substring.foodies.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    List<Restaurant> findByName(String name);

    @Query(value = "SELECT * FROM foodie_restaurant WHERE is_Open = true", nativeQuery = true)
    Page<Restaurant> findAllOpenRestaurants(Pageable pageable);

    List<Restaurant> findByNameLike(String pattern);

    @Query(value = "select r from Restaurant r where CURRENT_TIMESTAMP >= r.openTime and CURRENT_TIMESTAMP < r.closeTime")
    List<Restaurant> findCurrentOpenRestaurants();

}
