package com.substring.foodies.service;

import com.substring.foodies.controller.AuthController;
import com.substring.foodies.dto.AddressDto;
import com.substring.foodies.dto.ChangePasswordDto;
import com.substring.foodies.dto.ChangeRoleDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.dto.enums.AddressType;
import com.substring.foodies.dto.enums.Role;
import com.substring.foodies.entity.Address;
import com.substring.foodies.entity.User;
import com.substring.foodies.exception.BadRequestException;
import com.substring.foodies.exception.ResourceNotFound;
import com.substring.foodies.repository.RestaurantRepository;
import com.substring.foodies.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ModelMapper modelMapper;

    private Logger logger= LoggerFactory.getLogger(AuthController.class);

    private User getLoggedInUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Invalid session"));
    }

    private User findAndValidate(String id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id = "+id));

        return user;
    }

    public UserDto updateUser(String userId, UserDto userDto) {

        User existingUser = findAndValidate(userId);

        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getRole() != Role.ROLE_ADMIN &&
                !loggedInUser.getId().equals(userId)) {

            throw new AccessDeniedException(
                    "You can update only your own profile"
            );
        }

        // ✅ Allowed profile fields only
        existingUser.setName(userDto.getName());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setGender(userDto.getGender());

        // ✅ Address update (NO addressId)
        if (userDto.getAddress() != null) {
            Address address = existingUser.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(existingUser);
            }

            AddressDto a = userDto.getAddress();
            address.setAddressLine(a.getAddressLine());
            address.setCity(a.getCity());
            address.setState(a.getState());
            address.setPincode(a.getPincode());
            address.setCountry(a.getCountry());

            existingUser.setAddress(address);
        }

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable page) {
        Page<User> userPage = userRepository.findAll(page);
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public List<UserDto> getUserByName(String userName) {
        List<User> users = userRepository.findByName(userName);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFound(
                                String.format("User not found with email = %s", userEmail)
                        )
                );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {

        User user = findAndValidate(userId);
        UserDto userDto=modelMapper.map(user, UserDto.class);
        return userDto;
    }

    public void deleteUser(String userId) {

        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getRole() != Role.ROLE_ADMIN &&
                !loggedInUser.getId().equals(userId)) {

            throw new AccessDeniedException(
                    "You can delete only your own account"
            );
        }

        findAndValidate(userId);

        userRepository.deleteById(userId);
    }


    @Override
    public UserDto signUpUser(UserDto dto) {

        if (userRepository.existsById(dto.getId())) {
            throw new IllegalStateException(
                    "User already exists with id = " + dto.getId()
            );
        }

        // 1️⃣ Validate address
        if (dto.getAddress() == null) {
            throw new BadRequestException("Address is required");
        }

        AddressDto addressDto = dto.getAddress();

        if (addressDto.getAddressLine() == null || addressDto.getAddressLine().isBlank()) {
            throw new BadRequestException("Address line is required");
        }

        if (addressDto.getCity() == null || addressDto.getCity().isBlank()) {
            throw new BadRequestException("City is required");
        }

        if (addressDto.getPincode() == null || !addressDto.getPincode().matches("\\d{6}")) {
            throw new BadRequestException("Invalid pincode");
        }

        // 2️⃣ Map & save user
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3️⃣ Own the address
        Address address = user.getAddress();
        address.setUser(user);
        address.setAddressType(AddressType.USER);// one-to-one ownership

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }


    public UserDto patchUser(String userId, UserDto patchDto) {

        User loggedInUser = getLoggedInUser();
        User user = findAndValidate(userId);

        if (loggedInUser.getRole() != Role.ROLE_ADMIN &&
                !loggedInUser.getId().equals(userId)) {

            throw new AccessDeniedException(
                    "You can patch only your own profile"
            );
        }

        if (patchDto.getName() != null) {
            user.setName(patchDto.getName());
        }

        if (patchDto.getPhoneNumber() != null) {
            user.setPhoneNumber(patchDto.getPhoneNumber());
        }

        if (patchDto.getGender() != null) {
            user.setGender(patchDto.getGender());
        }

        // Address patch (NO addressId)
        if (patchDto.getAddress() != null) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(user);
            }

            AddressDto a = patchDto.getAddress();
            if (a.getAddressLine() != null) address.setAddressLine(a.getAddressLine());
            if (a.getCity() != null) address.setCity(a.getCity());
            if (a.getState() != null) address.setState(a.getState());
            if (a.getPincode() != null) address.setPincode(a.getPincode());
            if (a.getCountry() != null) address.setCountry(a.getCountry());

            user.setAddress(address);
        }

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }


    @Override
    @Transactional
    public void changeUserRole(String userId, ChangeRoleDto dto) {

        User admin = getLoggedInUser();

        // 1️⃣ Only ADMIN can change roles
        if (admin.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Admin access required");
        }

        // 2️⃣ Role must be provided
        if (dto.getRole() == null) {
            throw new BadRequestException("Role cannot be null");
        }

        User user = findAndValidate(userId);

        // 3️⃣ Cannot modify another ADMIN
        if (user.getRole() == Role.ROLE_ADMIN && !admin.getId().equals(userId)) {
            throw new AccessDeniedException("Cannot modify another admin");
        }

        // 4️⃣ Null-safe restaurant ownership check
        boolean ownsRestaurant =
                user.getRestaurantList() != null &&
                        !user.getRestaurantList().isEmpty();

        // 5️⃣ Restaurant owner downgrade protection
        if (ownsRestaurant &&
                (dto.getRole() == Role.ROLE_USER ||
                        dto.getRole() == Role.ROLE_DELIVERY_BOY)) {

            throw new BadRequestException(
                    "Restaurant owner cannot be downgraded to USER or DELIVERY_BOY"
            );
        }

        // 6️⃣ Apply role change
        user.setRole(dto.getRole());
        userRepository.save(user);
    }


    @Override
    public void changePassword(String userId, ChangePasswordDto dto) {

        User user = findAndValidate(userId);
        User loggedInUser = getLoggedInUser();

        if (!loggedInUser.getId().equals(userId)) {
            throw new AccessDeniedException(
                    "You can change only your own password."
            );
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Old password is incorrect.");
        }


        if(!dto.getNewPassword().equals(dto.getConfirmPassword()))
        {
            throw new BadRequestException("Password and Confirm Password must match.");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
