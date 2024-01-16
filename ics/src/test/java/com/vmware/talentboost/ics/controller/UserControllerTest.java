package com.vmware.talentboost.ics.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.vmware.talentboost.ics.data.User;
import com.vmware.talentboost.ics.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	public void setup() {
		mockMvc = standaloneSetup(userController).build();
	}

	@Test
	public void testRegister() throws Exception {
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("password");

		when(userService.register(any(User.class))).thenReturn(user);

		mockMvc.perform(post("/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("testUser"));

		verify(userService).register(any(User.class));
	}

	@Test
	public void testLogin() throws Exception {
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("password");

		when(userService.authenticate("testUser", "password")).thenReturn(user);

		mockMvc.perform(post("/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testUser"));

		verify(userService).authenticate("testUser", "password");
	}
}
