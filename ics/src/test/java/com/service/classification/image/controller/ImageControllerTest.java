package com.service.classification.image.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.classification.image.data.Image;
import com.service.classification.image.data.User;
import com.service.classification.image.dto.ImageUploadRequest;
import com.service.classification.image.service.ImageService;
import com.service.classification.image.service.UserService;
import com.service.classification.image.dto.ImageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ImageService imageService;

	@Mock
	private UserService userService;

	@InjectMocks
	private ImageController imageController;

	@BeforeEach
	public void setup() {
		mockMvc = standaloneSetup(imageController).build();
	}

	@Test
	public void testGetImagesByUser() throws Exception {
		String username = "testUser";
		List<Image> images = Arrays.asList(new Image(), new Image()); // Mock some image data
		when(imageService.getImagesByUser(username)).thenReturn(images);

		mockMvc.perform(get("/images/user/" + username)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[1]").exists());

		verify(imageService).getImagesByUser(username);
	}

	@Test
	public void testGetAllImages() throws Exception {
		List<Image> images = Arrays.asList(new Image(), new Image()); // Mock image data
		when(imageService.getAllImages()).thenReturn(images);

		mockMvc.perform(get("/images")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[1]").exists());

		verify(imageService).getAllImages();
	}

	@Test
	public void testGetImageById() throws Exception {
		Integer imageId = 1;
		Image image = new Image(); // Mock image
		when(imageService.getImageById(imageId)).thenReturn(image);

		mockMvc.perform(get("/images/" + imageId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

		verify(imageService).getImageById(imageId);
	}

	@Test
	public void testGetImageByURL() throws Exception {
		String imageUrl = "http://example.com/image.jpg";
		Image image = new Image();
		when(imageService.getImageByURL(imageUrl)).thenReturn(image);

		mockMvc.perform(get("/images/url?url=" + imageUrl)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

		verify(imageService).getImageByURL(imageUrl);
	}

	@Test
	public void testUpdateImage() throws Exception {
		String imageUrl = "http://example.com/image.jpg";
		ImageUploadRequest request = new ImageUploadRequest();
		request.imageUrl = imageUrl;

		String username = "user";
		String password = "pass";

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		Integer imageId = 1;
		Image updatedImage = new Image();
		ImageDto imageDto = new ImageDto();
		imageDto.URL = imageUrl;
		imageDto.username = username;
		imageDto.addedOn = Timestamp.valueOf(LocalDateTime.now());
		imageDto.service = "Test";
		imageDto.height = 100;
		imageDto.width = 100;
		imageDto.id = imageId;

		when(imageService.updateImage(eq(imageId), any(Image.class))).thenReturn(updatedImage);

		mockMvc.perform(put("/images/" + imageId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(imageDto)))
				.andExpect(status().isNoContent());
		verify(imageService).updateImage(eq(imageId), any(Image.class));

	}

	@Test
	public void testDeleteImage() throws Exception {
		Integer imageId = 1;

		doNothing().when(imageService).deleteImage(imageId);

		mockMvc.perform(delete("/images/" + imageId))
				.andExpect(status().isNoContent());

		verify(imageService).deleteImage(imageId);

	}

}