package com.substring.foodies.controller;

import com.substring.foodies.dto.*;
import com.substring.foodies.repository.UserRepository;
import com.substring.foodies.security.JwtService;
import com.substring.foodies.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // We use SLF4j logger, LoggerFactory is the static method and getLogger is its method
    // We then have to provide our class name to get to know where the logger is used.
    private Logger logger=LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto)
    {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
        authenticationManager.authenticate(token);

        String jwtAccessToken = jwtService.generateToken(loginUserDto.email(), true);
        String jwtRefreshToken = jwtService.generateToken(loginUserDto.email(), false);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.email());
        UserDto userDto = modelMapper.map(userService.getUserByEmail(userDetails.getUsername()), UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .user(userDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        if(!jwtService.isRefreshToken(refreshTokenRequest.getRefreshToken()))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }

        if(jwtService.isTokenValid(refreshTokenRequest.getRefreshToken()) && jwtService.isRefreshToken(refreshTokenRequest.getRefreshToken()))
        {
            String username = jwtService.getUsername(refreshTokenRequest.getRefreshToken());
            UserDto userDto = modelMapper.map(userRepository.findByEmail(username), UserDto.class);
            String jwtAccessToken = jwtService.generateToken(username, true);
            String jwtRefreshToken = jwtService.generateToken(username, false);

            JwtResponse response = JwtResponse.builder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .user(userDto)
                    .build();
            return ResponseEntity.ok(response);
        }

        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }
    }



    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpUserDto signUpUserDto) {

        if (!signUpUserDto.getPassword().equals(signUpUserDto.getConfirmPassword())) {
            ErrorResponse response = ErrorResponse.builder()
                    .message("Password and Confirm Password do not match")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

            return ResponseEntity.badRequest().body(response);  // fixed this too
        }

        SignUpUserDto user = userService.signUpUser(signUpUserDto);
        return ResponseEntity.ok(user);  // returning the created user, not the request again
    }


    // Exception handling method: for this controller.
    // These below methods are Local Exception Handlers.
    @ExceptionHandler(NullPointerException.class)
    public String handleNUllPointerException(NullPointerException ex)
    {
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return ex.getMessage();
    }

}
