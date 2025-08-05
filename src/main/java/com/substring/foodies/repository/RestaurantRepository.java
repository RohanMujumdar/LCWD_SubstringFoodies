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

    List<Restaurant> findByisActive(boolean isActive);
    List<Restaurant> findByIsOpen(boolean isOpen);
    List<Restaurant> findByIsOpenAndIsActive(boolean isActive, boolean isOpen);
    List<Restaurant> findByOwnerId(String ownerId);

    @Query(value = "SELECT * FROM foodie_restaurant WHERE is_Open = true", nativeQuery = true)
    Page<Restaurant> findAllOpenRestaurants(Pageable pageable);

    List<Restaurant> findByNameContainingIgnoreCase(String pattern);



}
