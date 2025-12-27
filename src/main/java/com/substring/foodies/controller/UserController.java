package com.substring.foodies.controller;

import com.substring.foodies.dto.ChangePasswordDto;
import com.substring.foodies.dto.ChangeRoleDto;
import com.substring.foodies.dto.ErrorResponse;
import com.substring.foodies.dto.UserDto;
import com.substring.foodies.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.savedUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(value="size", required = false, defaultValue = "10") int size,
                                                     @RequestParam(value="sortBy", required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(value="sortDir", required = false, defaultValue = "asc") String sortDir)
    {
        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page, size, sort);

        Page<UserDto> allUsers = userService.getAllUsers(pageable);
        return new ResponseEntity<>(allUsers,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id)
    {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsersByName(
            @RequestParam("name") String name
    ) {
        List<UserDto> users = userService.getUserByName(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable String id, @RequestBody UserDto userDto)
    {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            ErrorResponse response = ErrorResponse.builder()
                    .message("Password and Confirm Password do not match")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

            return ResponseEntity.badRequest().body(response);  // fixed this too
        }
        UserDto user = userService.updateUser(id, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<?> patchUser(
            @PathVariable String id,
            @RequestBody UserDto userDto
    ) {

        UserDto updatedUser = userService.patchUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/role-change")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable String id, @RequestBody ChangeRoleDto dto)
    {
        userService.changeUserRole(id, dto);
        return new ResponseEntity<>("Role changed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteMyAccount/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id)
    {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
