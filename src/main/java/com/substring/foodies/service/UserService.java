package com.substring.foodies.service;


import com.substring.foodies.dto.SignUpUserDto;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface UserService {


    UserDto updateUser(String id, UserDto userDto);


    UserDto savedUser(UserDto userDto);
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto updateSavedUser(UserDto userDto, String id);
    List<UserDto> getUserByName(String userName);
    UserDto getUserByEmail(String userEmail);
    UserDto getUserById (String userId);
    void deleteUser(String userId);

    SignUpUserDto signUpUser(SignUpUserDto signUpUserDto);
    List<UserDto> searchUserName(String keyword);
}
