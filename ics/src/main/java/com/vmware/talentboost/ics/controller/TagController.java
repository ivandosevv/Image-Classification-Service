package com.vmware.talentboost.ics.controller;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.dto.ImageDto;
import com.vmware.talentboost.ics.dto.TagDto;
import com.vmware.talentboost.ics.service.ImageService;
import com.vmware.talentboost.ics.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tags")
@CrossOrigin(origins = "http://localhost:4200")
public class TagController {
	private final TagService tagService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);

	@Autowired
	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	@GetMapping
	public ResponseEntity<List<Tag>> getAllTags() {
		LOGGER.info("Fetching all tags");
		List<Tag> tags = tagService.getAllTags();
		return ResponseEntity.ok(tags);
	}

	@GetMapping(value = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Image>> getImagesByTagName(@PathVariable String name) {
		LOGGER.info("Fetching images by tag name: {}", name);
		try {
			List<Connection> connections = tagService.getConnectionsByTagName(name);
			List<Image> images = connections.stream()
					.map(Connection::getImage)
					.collect(Collectors.toList());
			return ResponseEntity.ok(images);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final TagDto tagDto) {
		LOGGER.info("Creating a new tag: {}", tagDto.name);
        if (!StringUtils.hasText(tagDto.name)) {
            throw new IllegalArgumentException("Tag name must be specified.");
        }

        final Tag tagToCreate = new Tag();
        tagToCreate.setName(tagDto.name);

        tagService.addTag(tagToCreate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable final Integer id) {
		LOGGER.info("Deleting tag with ID: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Tag ID must be specified.");
        }
        try {
            tagService.deleteTag(id);
        } catch (final EmptyResultDataAccessException e) {
            throw new NoSuchElementException(
                String.format("No name with ID %d found.", id));
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> edit(@PathVariable final Integer id,
                                     @RequestBody final TagDto tagDto) {
		LOGGER.info("Editing tag with ID: {}", id);
        if (!StringUtils.hasText(tagDto.name)) {
            throw new IllegalArgumentException("Tag title must be specified.");
        }
        if (id == null) {
            throw new IllegalArgumentException("Tag ID must be specified.");
        }
        final Tag tagToUpdate = new Tag();
        tagToUpdate.setName(tagDto.name);
        tagService.updateTag(id, tagToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
