package com.vmware.talentboost.ics.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.dto.TagDto;
import com.vmware.talentboost.ics.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
	private MockMvc mockMvc;

	@Mock
	private TagService tagService;

	@InjectMocks
	private TagController tagController;

	@BeforeEach
	public void setup() {
		mockMvc = standaloneSetup(tagController).build();
	}

	@Test
	public void testGetAllTags() throws Exception {
		List<Tag> tags = Arrays.asList(new Tag(), new Tag());
		when(tagService.getAllTags()).thenReturn(tags);

		mockMvc.perform(get("/tags")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0]").exists())
				.andExpect(jsonPath("$[1]").exists());

		verify(tagService).getAllTags();
	}

	@Test
	public void testGetImagesByTagName() throws Exception {
		String tagName = "nature";
		// Assuming getImagesByTagName returns a list of images
		// Mock the response from tagService

		mockMvc.perform(get("/tags/" + tagName)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// Add appropriate JSON path checks
				.andExpect(jsonPath("$").isArray());

		verify(tagService).getConnectionsByTagName(tagName);
	}

	@Test
	public void testCreateTag() throws Exception {
		TagDto tagDto = new TagDto();
		tagDto.name = "nature";

		mockMvc.perform(post("/tags")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(tagDto)))
				.andExpect(status().isNoContent());

		verify(tagService).addTag(any(Tag.class));
	}

	@Test
	public void testDeleteTag() throws Exception {
		Integer tagId = 1;

		mockMvc.perform(delete("/tags/" + tagId))
				.andExpect(status().isNoContent());

		verify(tagService).deleteTag(tagId);
	}

	@Test
	public void testEditTag() throws Exception {
		Integer tagId = 1;
		TagDto tagDto = new TagDto();
		tagDto.name = "updatedName";

		mockMvc.perform(put("/tags/" + tagId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(tagDto)))
				.andExpect(status().isNoContent());

		verify(tagService).updateTag(eq(tagId), any(Tag.class));
	}


}