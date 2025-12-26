package com.substring.foodies.service;

import com.substring.foodies.controller.AuthController;
import com.substring.foodies.converter.Converter;
import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

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

    private Logger logger= LoggerFactory.getLogger(AuthController.class);

    public UserDto updateUser(String userId, UserDto userDto)
    {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound(String.format("User not found with id = %s", userId)));

        // Update the fields (excluding ID, createdDate, and associations unless needed)
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setRole(userDto.getRole());
        existingUser.setAvailable(userDto.isAvailable());
        existingUser.setEnabled(userDto.isEnabled());

        AddressDto addressDto = userDto.getAddress();
        existingUser.setAddress(modelMapper.map(addressDto, Address.class));
        existingUser.getAddress().setUser(existingUser);
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
    public List<UserDto> getUserByName(String userName) {

        List<User> user=userRepository.findByName(userName);
        return user.stream().map(newUser->converter.entityToDto(newUser)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new ResourceNotFound(String.format("User not found with email = %s", userEmail)));
        return converter.entityToDto(user);
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFound(String.format("User not found with id = %s", userId)));
        UserDto userDto=modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public void deleteUser(String userId) {

        userRepository.findById(userId).orElseThrow(()->new ResourceNotFound(String.format("User not found with id = %s", userId)));
        userRepository.deleteById(userId);

    }

    @Override
    public UserDto signUpUser(UserDto signUpUserDto) {

        User savedUser = modelMapper.map(signUpUserDto, User.class);
        savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));
        savedUser.getAddress().setUser(savedUser);
        return modelMapper.map(userRepository.save(savedUser), UserDto.class);
    }

    @Override
    public UserDto patchUser(String userId, UserDto patchDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFound("User not found with id = " + userId)
                );

        // name
        if (patchDto.getName() != null) {
            user.setName(patchDto.getName());
        }

        // phone
        if (patchDto.getPhoneNumber() != null) {
            user.setPhoneNumber(patchDto.getPhoneNumber());
        }

        // gender
        if (patchDto.getGender() != null) {
            user.setGender(patchDto.getGender());
        }

        // role (ONLY allow from ADMIN in controller)
        if (patchDto.getRole() != null) {
            user.setRole(patchDto.getRole());
        }

        // address patch (optional but clean)
        if (patchDto.getAddress() != null) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(user);
            }

            AddressDto addr = patchDto.getAddress();
            if (addr.getId() != null) address.setId(addr.getId());
            if (addr.getAddressLine() != null) address.setAddressLine(addr.getAddressLine());
            if (addr.getCity() != null) address.setCity(addr.getCity());
            if (addr.getState() != null) address.setState(addr.getState());
            if (addr.getPincode() != null) address.setPincode(addr.getPincode());
            if (addr.getCountry() != null) address.setCountry(addr.getCountry());

            user.setAddress(address);
        }

        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDto.class);
    }
}
