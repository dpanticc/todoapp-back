package com.iteh.todobackend.controller;

import com.iteh.todobackend.dto.UserRegistrationDto;
import com.iteh.todobackend.entity.User;
import com.iteh.todobackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;


@Api(tags = "User Management")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationRequest) {
        // Validate the userDto (e.g., check if the required fields are provided)

        // Create a new User entity based on the userDto data
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());

        // Save the user to the database using the UserService
        userService.registerUser(user);

        // Return a success message or status code
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
}
