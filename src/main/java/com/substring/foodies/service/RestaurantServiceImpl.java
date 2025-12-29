package com.substring.foodies.service;

import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.dto.FileData;
import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.AddressRepository;
import com.substring.foodies.repository.FoodItemRepository;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private FileService fileService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${restaurant.file.path}")
    private String bannerFolderpath;

    @Value("${spring.app.base-url}")
    private String baseUrl;

    @Override
    public RestaurantDto addRestaurant(RestaurantDto restaurantDto) {

        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);

        User owner = userRepository.findById(restaurantDto.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFound("User not found with id = " + restaurantDto.getOwnerId()));
        restaurant.setOwner(owner);

        List<String> addressIds = restaurantDto.getAddresses()
                .stream()
                .map(AddressDto::getId)
                .toList();

        List<Address> addressList = addressRepository.findAllById(addressIds);
        Set<String> foundIds = addressList.stream()
                .map(Address::getId)
                .collect(Collectors.toSet());

        List<String> missingIds = addressIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFound("Addresses not found with ids = " + missingIds);
        }

        for (Address address : addressList) {
            restaurant.getAddresses().add(address);
            address.getRestaurants().add(restaurant);
        }

        Restaurant saved = restaurantRepository.save(restaurant);
        return modelMapper.map(saved, RestaurantDto.class);
    }

    @Override
    public Page<RestaurantDto> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(resto->modelMapper.map(resto, RestaurantDto.class));
    }

    @Override
    public RestaurantDto updateSavedRestaurant(RestaurantDto restaurantDto, String id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Restaurant not found"));

        restaurant.setName(restaurantDto.getName());
        restaurant.setOpenTime(restaurantDto.getOpenTime());
        restaurant.setCloseTime(restaurantDto.getCloseTime());
        restaurant.setOpen(restaurantDto.isOpen());

        // ❌ Banner NOT updated here

        List<String> addressIds = restaurantDto.getAddresses()
                .stream()
                .map(AddressDto::getId)
                .toList();

        List<Address> addressList = addressRepository.findAllById(addressIds);
        Set<String> foundIds = addressList.stream()
                .map(Address::getId)
                .collect(Collectors.toSet());

        List<String> missingIds = addressIds.stream()
                .filter(idVal -> !foundIds.contains(idVal))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFound("Addresses not found with ids = " + missingIds);
        }

        for (Address address : restaurant.getAddresses()) {
            address.getRestaurants().remove(restaurant);
        }
        restaurant.getAddresses().clear();

        for (Address address : addressList) {
            restaurant.getAddresses().add(address);
            address.getRestaurants().add(restaurant);
        }

        Restaurant updated = restaurantRepository.save(restaurant);
        return modelMapper.map(updated, RestaurantDto.class);
    }

    @Override
    public Page<RestaurantDto> findByFoodItemsList_Id(String foodId, Pageable pageable) {
        return restaurantRepository
                .findByFoodItemsList_Id(foodId, pageable)
                .map(resto->modelMapper.map(resto, RestaurantDto.class));
    }

    @Override
    public RestaurantDto getRestaurantById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Restaurant not found"));
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public void deleteRestaurant(String id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFound("Restaurant not found with id = " + id);
        }
        restaurantRepository.deleteById(id);
    }

    @Override
    public Page<RestaurantDto> getAllOpenRestaurants(Pageable pageable) {
        return restaurantRepository
                .findAllOpenRestaurants(pageable)
                .map(resto->modelMapper.map(resto, RestaurantDto.class));
    }

    @Override
    public List<RestaurantDto> findByNameContainingIgnoreCase(String pattern) {
        return restaurantRepository.findByNameContainingIgnoreCase(pattern)
                .stream()
                .map(resto->modelMapper.map(resto, RestaurantDto.class))
                .toList();
    }

    @Override
    public RestaurantDto uploadBanner(MultipartFile file, String id) throws IOException {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Restaurant not found with id = " + id));

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = System.currentTimeMillis() + extension;

        Path oldFilePath = Paths.get(bannerFolderpath + restaurant.getBanner());
        if (Files.exists(oldFilePath)) {
            Files.delete(oldFilePath);
        }

        FileData fileData = fileService.uploadFile(file, bannerFolderpath + newFileName);
        restaurant.setBanner(fileData.getFileName());

        restaurantRepository.save(restaurant);
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public void deleteBanner(String path) {
        try {
            fileService.deleteFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestaurantDto> getByOwner(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .stream()
                .map(resto->modelMapper.map(resto, RestaurantDto.class))
                .toList();
    }
}
