package com.substring.foodies.service;

import com.substring.foodies.converter.Converter;
import com.substring.foodies.dto.SignUpUserDto;
import com.substring.foodies.dto.UserDto;
//import com.substring.foodies.entity.RoleEntity;
import com.substring.foodies.dto.enums.Role;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.RestaurantRepository;
//import com.substring.foodies.repository.RoleRepository;
import com.substring.foodies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private Converter converter;

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    private RoleRepository roleRepository;

    @Override
    public void save()
    {
//        User dummyUser = new User();
//        dummyUser.setId("user123");
//        dummyUser.setName("John Doe");
//        dummyUser.setEmail("john.doe@example.com");
//        dummyUser.setPassword("securePassword");
//        dummyUser.setAddress("123 Foodie Lane, Bangalore");
//        dummyUser.setPhoneNumber("9876543210");
//        dummyUser.setRole(Role.CUSTOMER); // Assuming Role.CUSTOMER is a valid role
//        dummyUser.setAvailable(true);
//
//        // Optionally add dummy Restaurant objects
//        List<Restaurant> restaurants = new ArrayList<>();
//
//        Restaurant restaurant1 = new Restaurant();
//        restaurant1.setId("resto1");
//        restaurant1.setName("The Gourmet Kitchen");
//        restaurant1.setAddress("456 Culinary Street, Bangalore");
//        restaurant1.setOpenTime(LocalTime.of(9, 0)); // Opens at 9:00 AM
//        restaurant1.setCloseTime(LocalTime.of(22, 0)); // Closes at 10:00 PM
//        restaurant1.setOpen(true); // Restaurant is currently open
//        restaurant1.setUser(dummyUser); // Associate with the dummy user
//
//        Restaurant restaurant2 = new Restaurant();
//        restaurant2.setId("resto2");
//        restaurant2.setName("Biryani Paradise");
//        restaurant2.setAddress("789 Spice Lane, Bangalore");
//        restaurant2.setOpenTime(LocalTime.of(10, 0)); // Opens at 10:00 AM
//        restaurant2.setCloseTime(LocalTime.of(23, 0)); // Closes at 11:00 PM
//        restaurant2.setOpen(true); // Restaurant is currently open
//        restaurant2.setUser(dummyUser); // Associate with the dummy user
//
//        Restaurant restaurant3 = new Restaurant();
//        restaurant3.setId("resto3");
//        restaurant3.setName("Tandoori Treats");
//        restaurant3.setAddress("321 Tandoor Avenue, Bangalore");
//        restaurant3.setOpenTime(LocalTime.of(11, 0)); // Opens at 11:00 AM
//        restaurant3.setCloseTime(LocalTime.of(21, 0)); // Closes at 9:00 PM
//        restaurant3.setOpen(false); // Restaurant is currently closed
//        restaurant3.setUser(dummyUser); // Associate with the dummy user
//
//        Restaurant restaurant4 = new Restaurant();
//        restaurant4.setId("resto4");
//        restaurant4.setName("Pizza Plaza");
//        restaurant4.setAddress("654 Pizza Road, Bangalore");
//        restaurant4.setOpenTime(LocalTime.of(12, 0)); // Opens at 12:00 PM
//        restaurant4.setCloseTime(LocalTime.of(22, 30)); // Closes at 10:30 PM
//        restaurant4.setOpen(true); // Restaurant is currently open
//        restaurant4.setUser(dummyUser); // Associate with the dummy user
//
//        Restaurant restaurant5 = new Restaurant();
//        restaurant5.setId("resto5");
//        restaurant5.setName("Sushi Spot");
//        restaurant5.setAddress("987 Sushi Lane, Bangalore");
//        restaurant5.setOpenTime(LocalTime.of(10, 30)); // Opens at 10:30 AM
//        restaurant5.setCloseTime(LocalTime.of(22, 0)); // Closes at 10:00 PM
//        restaurant5.setOpen(true); // Restaurant is currently open
//        restaurant5.setUser(dummyUser); // Associate with the dummy user
//
//        // Add restaurants to the user
//        restaurants.add(restaurant1);
//        restaurants.add(restaurant2);
//        restaurants.add(restaurant3);
//        restaurants.add(restaurant4);
//        restaurants.add(restaurant5);
//
//        dummyUser.setRestaurantList(restaurants);
//
//        // Save the user (along with associated restaurants)
//        userRepository.save(dummyUser);
//
//        // Print the dummy user for verification
//        System.out.println(dummyUser);
    }

    @Transactional
    public void updateUser()
    {
//        User savedUser=userRepository.findById("user123").orElseThrow(()->new RuntimeException("No User Found"));
//
//        Restaurant restaurant5 = new Restaurant();
//        restaurant5.setId("resto6");
//        restaurant5.setName("Sushi Palace");
//        restaurant5.setAddress("677 Machi Nagar Lane, Pune");
//        restaurant5.setOpenTime(LocalTime.of(10, 30)); // Opens at 10:30 AM
//        restaurant5.setCloseTime(LocalTime.of(22, 0)); // Closes at 10:00 PM
//        restaurant5.setOpen(true); // Restaurant is currently open
//
//        // Associate this restaurant with a user (optional, replace dummyUser with the actual user if required)
//        restaurant5.setUser(savedUser);
//        savedUser.getRestaurantList().add(restaurant5);
//        userRepository.save(savedUser);
    }
//
//    @Override
//    public void testUserRole() {
//
//        User dummyUser = new User();
//        dummyUser.setId("user124");
//        dummyUser.setName("Ravi Kumar");
//        dummyUser.setEmail("ravikumar@example.com");
//        dummyUser.setPassword("securePassword123..");
//
//        dummyUser.setPhoneNumber("9876543210");
//        dummyUser.setAvailable(true);
//
//        RoleEntity roleAdmin = new RoleEntity();
//        roleAdmin.setId(1);
//        roleAdmin.setName("ROLE_ADMIN");
//
//        RoleEntity roleGuest = new RoleEntity();
//        roleGuest.setId(2);
//        roleGuest.setName("ROLE_GUEST");
//
//        // Add these roles to a list or save them to the database as needed.
//        List<RoleEntity> roleList = List.of(roleAdmin, roleGuest);
//
//
//        // Setting the users via entity
//        roleList.forEach(roleEntity -> {
//            roleEntity.getUserList().add(dummyUser);
//        });
//
//        userRepository.save(dummyUser);
//    }

    @Override
    public UserDto savedUser(UserDto userDto) {

        User savedUser = new User();
        savedUser=converter.dtoToEntity(userDto);
        savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));

//        RoleEntity roleEntity=roleRepository.findByName(AppConstants.ROLE_GUEST);
//        savedUser.getRoleEntityList().add(roleEntity);

        userRepository.save(savedUser);
        return converter.entityToDto(savedUser);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable page) {
        Page<User> userPage=userRepository.findAll(page);
        return userPage.map(user->converter.entityToDto(user));
    }

    @Override
    public UserDto updateSavedUser(UserDto userDto, String id) {
        return null;
    }

    @Override
    public List<UserDto> getUserByName(String userName) {

        List<User> user=userRepository.findByName(userName);
        return user.stream().map(newUser->converter.entityToDto(newUser)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
//                .orElseThrow(()->new ResourceNotFound("User not found"));
        return converter.entityToDto(user);
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFound());
//        List<String> roleNames = user.getRoleEntityList()
//                .stream()
//                .map(RoleEntity::getName)
//                .toList();
//        System.out.println(roleNames);
//
        UserDto userDto=modelMapper.map(user, UserDto.class);
//
//        List<RoleEntityDto> roleDtos = user.getRoleEntityList()
//                .stream()
//                .map(role -> modelMapper.map(role, RoleEntityDto.class))
//                .toList();
//
//        userDto.setRoleEntityDtoList(roleDtos);

        return userDto;
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) {
        User savedUser = modelMapper.map(signUpUserDto, User.class);
        savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));
        savedUser.setRole(Role.USER);

        userRepository.save(savedUser);
        return  signUpUserDto;
    }

    @Override
    public List<UserDto> searchUserName(String keyword) {
        return List.of();
    }

}
