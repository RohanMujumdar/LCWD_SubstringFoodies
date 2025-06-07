package com.substring.foodies.controller;

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


@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto)
    {
        UserDto userDto1 = userService.savedUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }


    @GetMapping("/")
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
    public ResponseEntity<UserDto> getUserById(@PathVariable String id)
    {
//        throw new NullPointerException("Please enter a valid number");
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
