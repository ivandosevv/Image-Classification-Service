package com.vmware.talentboost.ics.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.transaction.Transactional;

import com.vmware.talentboost.ics.analyzer.ImageAnalyzer;
import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.data.User;
import com.vmware.talentboost.ics.dto.ImageDto;
import com.vmware.talentboost.ics.dto.ImageUploadRequest;
import com.vmware.talentboost.ics.service.ConnectionService;
import com.vmware.talentboost.ics.service.ImageService;
import com.vmware.talentboost.ics.service.TagService;
import com.vmware.talentboost.ics.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("images")
public class ImageController {
	private final ImageService imageService;

	private final TagService tagService;

	private final ConnectionService connectionService;

	private final UserService userService;

	private final int MIN_CONFIDENCE = 30;

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	public ImageController(ImageService imageService, TagService tagService, ConnectionService connectionService,
			UserService userService) {
		this.imageService = imageService;
		this.tagService = tagService;
		this.connectionService = connectionService;
		this.userService = userService;
	}

	@GetMapping
	public List<Image> get(@RequestParam(required = false) Optional<List<Tag>> tags) {
		if (tags.isPresent()) {
			LOGGER.info("Fetching images with specific tags: {}", tags);
			List<Tag> myTags = tags.get();
			List<Image> myImages = new ArrayList<>();

			for (Tag tag : myTags) {
				List<Connection> currConnections = tag.getConnections();

				for (Connection currConnection : currConnections) {
					myImages.add(currConnection.getImage());
				}
			}

			return myImages;
		}

		LOGGER.info("Fetching all images, since tags list is empty");
		return imageService.getAllImages();
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<List<Image>> getImagesByUser(@PathVariable String username) {
		LOGGER.info("Fetching images for user: {}", username);
		List<Image> userImages = imageService.getImagesByUser(username);
		return new ResponseEntity<>(userImages, HttpStatus.OK);
	}

	@GetMapping("{id}")
	public Image get(@PathVariable Integer id) {
		LOGGER.info("Fetching image with ID: {}", id);
		try {
			return imageService.getImageById(id);
		}
		catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Image with ID %d not found", id));
		}
	}

	@GetMapping("url")
	@ResponseBody
	public Image get(@RequestParam String url) {
		LOGGER.info("Fetching image with URL: {}", url);
		try {
			return imageService.getImageByURL(url);
		}
		catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Image with URL %d not found", url));
		}
	}

	@GetMapping("id/{id}/tags")
	public Map<String, Double> getTags(@PathVariable final Integer id) {
		LOGGER.info("Fetching tags for image with ID: {}", id);
		List<Connection> connections = this.imageService.getConnectionsByImageId(id);
		Map<String, Double> pairs = new HashMap<>();
		for (Connection connection : connections) {
			Tag tag = this.tagService.getTagById(connection.getTag().getId());
			pairs.put(tag.getName(), connection.getConfidence());
		}

		return pairs;
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Image> create(@RequestBody final ImageUploadRequest request) throws IOException {
		LOGGER.info("Received image upload request: {}", request);
		User user = userService.authenticate(request.username, request.password);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String imageURL = request.imageUrl;
		if (!StringUtils.hasText(imageURL)) {
			throw new IllegalArgumentException("Image URL must be specified.");
		}

		Long datetime = System.currentTimeMillis();
		URL url = new URL(imageURL);
		java.awt.Image realImage = new ImageIcon(url).getImage();
		Image toAdd = new Image();
		CompletableFuture<Image> imageFuture;
		try {
			toAdd = new Image(imageURL, new Timestamp(datetime), "Imagga", realImage.getWidth(null),
					realImage.getHeight(null), user);

			imageFuture = this.imageService.addImage(toAdd);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String jsonResponse = ImageAnalyzer.sendRequestToAnalyzer(imageURL);
		System.out.println(jsonResponse);
		JSONObject obj = new JSONObject(jsonResponse);
		JSONObject result = obj.getJSONObject("result");
		JSONArray arr = result.getJSONArray("tags");

		for (int i = 0; i < arr.length(); i++) {
			JSONObject conf = arr.getJSONObject(i);
			double confidence = conf.getDouble("confidence");

			if (confidence < MIN_CONFIDENCE) {
				break;
			}

			JSONObject tag = conf.getJSONObject("tag");
			String name = tag.getString("en");

			Tag myTag = tagService.addTag(new Tag(name));
			connectionService.addImageTagConnection(new Connection(toAdd.getId(), myTag.getId(), toAdd, myTag, confidence));
		}

		try {
			Image resultImage = imageFuture.get();
			return ResponseEntity.ok(resultImage);
		}
		catch (InterruptedException | ExecutionException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(toAdd);
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable final Integer id) {
		LOGGER.info("Deleting image with ID: {}", id);
		if (id == null) {
			throw new IllegalArgumentException("Image ID must be specified.");
		}
		try {
			imageService.deleteImage(id);
		}
		catch (final EmptyResultDataAccessException e) {
			throw new NoSuchElementException(
					String.format("No image with ID %d found.", id));
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("{id}")
	public ResponseEntity<Void> edit(@PathVariable final Integer id,
			@RequestBody final ImageDto imageDto) {
		LOGGER.info("Updating image with ID: {}", id);
		if (!StringUtils.hasText(imageDto.URL)) {
			throw new IllegalArgumentException("Image title must be specified.");
		}
		if (id == null) {
			throw new IllegalArgumentException("Image ID must be specified.");
		}
		final Image imageToUpdate = new Image();
		imageToUpdate.setUrl(imageDto.URL);
		imageToUpdate.setService(imageDto.service);
		imageToUpdate.setAddedOn(imageDto.addedOn);
		imageToUpdate.setWidth(imageDto.width);
		imageToUpdate.setHeight(imageDto.height);
		imageService.updateImage(id, imageToUpdate);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
