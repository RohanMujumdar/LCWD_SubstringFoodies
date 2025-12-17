package com.substring.foodies.service;
import com.substring.foodies.converter.Converter;
import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.dto.FileData;
import com.substring.foodies.dto.RestaurantDto;
import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.Restaurant;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.AddressRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private FileService fileService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${restaurant.file.path}")
    private String bannerFolderpath;

    @Override
    public RestaurantDto addRestaurant(RestaurantDto restaurantDto) {

        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);

        // Set owner
        User owner = userRepository.findById(restaurantDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFound("User not found with id = "+restaurantDto.getOwnerId()));
        restaurant.setOwner(owner);

        List<String> addressIds = restaurantDto.getAddresses()
                                    .stream()
                                    .map(add->add.getId())
                                    .toList();

        List<Address> addressList = addressRepository.findAllById(addressIds);
        Set<String> set = addressList
                            .stream()
                            .map(add->add.getId())
                            .collect(Collectors.toSet());

        List<String> missingIds = addressIds.stream().filter(id->!set.contains(id)).toList();

        if(!missingIds.isEmpty())
        {
            throw new ResourceNotFound("Addresses not found with ids = "+missingIds);
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
         Page<Restaurant> restaurants= restaurantRepository.findAll(pageable);

         return restaurants.map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class));
    }

    @Override
    public RestaurantDto updateSavedRestaurant(RestaurantDto restaurantDto, String id) {

        Restaurant restaurant=restaurantRepository.findById(id).orElseThrow(()->new ResourceNotFound());

        restaurant.setName(restaurantDto.getName());
        restaurant.setOpenTime(restaurantDto.getOpenTime());
        restaurant.setCloseTime(restaurantDto.getCloseTime());
        restaurant.setOpen(restaurantDto.isOpen());
        restaurant.setBanner(restaurantDto.getBanner());

        List<String> addressIds = restaurantDto
                                    .getAddresses()
                                    .stream()
                                    .map(add->add.getId())
                                    .toList();

        List<Address> addressList = addressRepository.findAllById(addressIds);
        Set<String> set = addressList
                            .stream()
                            .map(add->add.getId())
                            .collect(Collectors.toSet());
        List<String> missingIds = addressIds
                                    .stream()
                                    .filter(addId->!set.contains(addId))
                                    .toList();

        if(!missingIds.isEmpty())
        {
            throw new ResourceNotFound("Addresses not found with ids = "+missingIds);
        }
        for (Address address : restaurant.getAddresses()) {
            address.getRestaurants().remove(restaurant);
        }
        restaurant.getAddresses().clear();

        for (Address address : addressList) {
            restaurant.getAddresses().add(address);
            address.getRestaurants().add(restaurant);
        }
        // Save the updated restaurant entity back to the database
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        // Convert the updated entity back to a DTO
        return converter.restoEntityToDto(updatedRestaurant);
    }

    @Override
    public List<RestaurantDto> findRestaurantByName(String name) {
        List<Restaurant> restaurantList = restaurantRepository.findByNameContainingIgnoreCase(name);

        return converter.restoEntityToDto(restaurantList);
    }

    @Override
    public List<RestaurantDto> findRestaurantByIsActive(Boolean isActive) {
        return List.of();
    }


    @Override
    public RestaurantDto getRestaurantById(String id) {

        Restaurant restaurant=restaurantRepository.findById(id).orElseThrow(()->new ResourceNotFound("Restaurant not found."));
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public Page<RestaurantDto> getAllOpenRestaurants(Pageable pageable) {
        Page<Restaurant> restaurants=restaurantRepository.findAllOpenRestaurants(pageable);

        return restaurants.map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class));
    }

    @Override
    public List<RestaurantDto> findByNameLikeRestaurants(String pattern) {
        List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(pattern);
        return restaurants.stream().map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class)).collect(toList());
    }

    @Override
    public List<RestaurantDto> findCurrentOpenAndActiveRestaurants(boolean isActive, boolean isOpen) {
        return restaurantRepository.findByIsOpenAndIsActive(isActive, isOpen).stream().map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class)).collect(toList());
    }

    @Override
    public RestaurantDto uploadBanner(MultipartFile file, String id) throws IOException {

        Restaurant restaurant=restaurantRepository.findById(id).orElseThrow(()->new ResourceNotFound());

        String fileName=file.getOriginalFilename();
        String fileExtension=fileName.substring(fileName.lastIndexOf('.'));
        String newFileName=new Date().getTime()+fileExtension;
        Path oldFilePath = Paths.get(bannerFolderpath+restaurant.getBanner());

        if(Files.exists(oldFilePath))
        {
            Files.delete(oldFilePath);
        }

        FileData fileData = fileService.uploadFile(file, bannerFolderpath+newFileName);
        restaurant.setBanner(fileData.getFileName());
        restaurantRepository.save(restaurant);
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public void deleteBanner(String path) {
        try {
            fileService.deleteFile(path);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestaurantDto> getByOwner(String ownerId) {

        List<Restaurant> restaurantList = restaurantRepository.findByOwnerId(ownerId);
        return restaurantList.stream().map(restaurant ->
            modelMapper.map(restaurant, RestaurantDto.class)
        ).toList();
    }

}
