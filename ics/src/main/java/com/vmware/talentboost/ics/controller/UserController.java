package com.vmware.talentboost.ics.controller;

import com.vmware.talentboost.ics.data.User;
import com.vmware.talentboost.ics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		User registeredUser = userService.register(user);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
		if (authenticatedUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return ResponseEntity.ok(authenticatedUser);
	}
}
