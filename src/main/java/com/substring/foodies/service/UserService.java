package com.substring.foodies.service;


import com.substring.foodies.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface UserService {


    UserDto updateUser(String id, UserDto userDto);


    UserDto savedUser(UserDto userDto);
    Page<UserDto> getAllUsers(Pageable pageable);
    List<UserDto> getUserByName(String userName);
    UserDto getUserByEmail(String userEmail);
    UserDto getUserById (String userId);
    UserDto patchUser(String userId, UserDto patchDto);
    void deleteUser(String userId);
    UserDto signUpUser(UserDto signUpUserDto);
}
