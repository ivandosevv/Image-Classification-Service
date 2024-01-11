package com.vmware.talentboost.ics.service;

import com.vmware.talentboost.ics.data.User;
import com.vmware.talentboost.ics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User register(User user) {
		// Hash the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// Save the user
		return userRepository.save(user);
	}

	public User authenticate(String username, String password) {
		User user = userRepository.findByUsername(username);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}
}
