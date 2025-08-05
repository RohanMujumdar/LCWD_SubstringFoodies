package com.substring.foodies.service;

import com.substring.foodies.converter.Converter;
import com.substring.foodies.dto.SignUpUserDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.dto.enums.Role;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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



    public UserDto updateUser(String id, UserDto userDto)
    {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No User Found"));

        // Update the fields (excluding ID, createdDate, and associations unless needed)
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setRole(userDto.getRole());
        existingUser.setAvailable(userDto.isAvailable());
        existingUser.setEnabled(userDto.isEnabled());

        // Save updated user
        User updatedUser = userRepository.save(existingUser);

        // Convert back to DTO and return
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto savedUser(UserDto userDto) {

        User savedUser = new User();
        savedUser=converter.dtoToEntity(userDto);
        savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));

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
        UserDto userDto=modelMapper.map(user, UserDto.class);
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
        savedUser.setRole(Role.ROLE_USER);

        userRepository.save(savedUser);
        return  signUpUserDto;
    }

    @Override
    public List<UserDto> searchUserName(String keyword) {
        return List.of();
    }

}
