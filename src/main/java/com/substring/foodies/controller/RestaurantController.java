package com.substring.foodies.controller;

import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    private Logger logger= LoggerFactory.getLogger(RestaurantController.class);

    @Value("${restaurant.file.path}")
    private String path;

    @PostMapping("/")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody RestaurantDto restaurantDto)
    {
        Restaurant restaurant=restaurantService.addRestaurant(restaurantDto);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable  String id)
    {
        RestaurantDto restaurantDto=restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurants(@RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(value="size", required = false, defaultValue = "6") int size,
                                                                 @RequestParam(value="sortBy", required = false, defaultValue = "id") String sortBy,
                                                                 @RequestParam(value="sortDir", required = false, defaultValue = "asc") String sortDir)
    {

        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page, size, sort);

        Page<RestaurantDto> restaurantDtoList=restaurantService.getAllRestaurants(pageable);

        return new ResponseEntity<>(restaurantDtoList, HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<Page<RestaurantDto>> getAllOpenRestaurants(@RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                                     @RequestParam(value="size", required = false, defaultValue = "2") int size,
                                                                     @RequestParam(value="sortBy", required = false, defaultValue = "id") String sortBy,
                                                                     @RequestParam(value="sortDir", required = false, defaultValue = "asc") String sortDir)

    {
        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page, size, sort);

        Page<RestaurantDto> restaurantDtoList=restaurantService.getAllOpenRestaurants(pageable);
        return new ResponseEntity<>(restaurantDtoList, HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<RestaurantDto>> searchRestaurantsByName(@RequestParam(value="name") String name)
    {
        List<RestaurantDto> restaurants = restaurantService.findByNameLikeRestaurants(name);

        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/currentlyOpen")
    public ResponseEntity<List<RestaurantDto>> findCurrentOpenRestaurants()
    {
        List<RestaurantDto> currentOpenRestaurants = restaurantService.findCurrentOpenRestaurants();
        return new ResponseEntity<>(currentOpenRestaurants, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@RequestBody RestaurantDto restaurantDto, @PathVariable String id)
    {
        // Check if the restaurant with the given ID exists
        RestaurantDto restaurantDto1 = restaurantService.updateSavedRestaurant(restaurantDto, id);

        return new ResponseEntity<>(restaurantDto1, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id)
    {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // API to handle restaurant banner;
    @PostMapping("/upload-banner/{restaurantId}")
    public ResponseEntity<?> uploadBanner(@RequestParam("banner") MultipartFile banner,
                                          @PathVariable String restaurantId)
    {
        try
        {
            RestaurantDto restaurantDto = restaurantService.uploadBanner(banner, restaurantId);
            return new ResponseEntity<>(restaurantDto, HttpStatus.OK);
        }
        catch(IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/deleteBanner/{fileName}")
    public void deleteBanner(@PathVariable String fileName)
    {
        String fullPath=path+fileName;
        restaurantService.deleteBanner(fullPath);
    }

    @GetMapping("/{restaurantId}/banner")
    public ResponseEntity<Resource> serveFile(@PathVariable String restaurantId) throws IOException
    {
        RestaurantDto restaurantDto=restaurantService.getRestaurantById(restaurantId);
        String fileName=restaurantDto.getBanner();
        String fullFilePath=path+fileName;

        Path actualFilePath= Paths.get(fullFilePath);
        Resource resource=new UrlResource(actualFilePath.toUri());
        if(resource.exists())
        {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        }else {
            throw new FileNotFoundException("File not found.");
        }
    }
}



