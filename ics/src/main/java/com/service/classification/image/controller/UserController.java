package com.service.classification.image.controller;

import com.service.classification.image.data.User;
import com.service.classification.image.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		LOGGER.info("Registering a new user: {}", user.getUsername());
		User registeredUser = userService.register(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		LOGGER.info("User login attempt: {}", user.getUsername());
		User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
		if (authenticatedUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return ResponseEntity.ok(authenticatedUser);
	}
}
