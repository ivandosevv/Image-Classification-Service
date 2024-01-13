package com.vmware.talentboost.ics.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.vmware.talentboost.ics.service.UserService;
import org.json.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("images")
public class ImageController {
    private final ImageService imageService;
    private final TagService tagService;
    private final ConnectionService connectionService;

	private final UserService userService;

    private final int MIN_CONFIDENCE = 30;

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
            List<Tag> myTags = tags.get();
            List<Image> myImages = new ArrayList<>();

            for (Tag tag: myTags) {
                List<Connection> currConnections = tag.getConnections();

                for (Connection currConnection: currConnections) {
                    myImages.add(currConnection.getImage());
                }
            }

            return myImages;
        }

        return imageService.getAllImages();
    }

    @GetMapping("{id}")
    public Image get(@PathVariable Integer id) {
        try {
            return imageService.getImageById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Image with ID %d not found", id));
        }
    }

    @GetMapping("url")
    @ResponseBody
    public Image get(@RequestParam String url) {
        try {
            System.out.println(url);
            return imageService.getImageByURL(url);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Image with URL %d not found", url));
        }
    }

    @GetMapping("id/{id}/tags")
    public Map<String, Double> getTags(@PathVariable final Integer id) {
        List<Connection> connections = this.imageService.getConnectionsByImageId(id);
        Map<String, Double> pairs = new HashMap<>();
        for (Connection connection: connections) {
            Tag tag = this.tagService.getTagById(connection.getTag().getId());
            pairs.put(tag.getName(), connection.getConfidence());
        }

        return pairs;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final ImageUploadRequest request) throws IOException {
		User user = userService.authenticate(request.username, request.password);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String imageURL = request.imageUrl;
        if (!StringUtils.hasText(imageURL)) {
            throw new IllegalArgumentException("Image URL must be specified.");
        }

        System.out.println(imageURL);
        Long datetime = System.currentTimeMillis();
        URL url = new URL(imageURL);
        java.awt.Image realImage = new ImageIcon(url).getImage();
        Image toAdd = new Image();
        try {
             toAdd = new Image(imageURL, new Timestamp(datetime), "Imagga", realImage.getWidth(null),
                realImage.getHeight(null));

            this.imageService.addImage(toAdd);
        } catch (Exception e) {
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

		return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable final Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Image ID must be specified.");
        }
        try {
            imageService.deleteImage(id);
        } catch (final EmptyResultDataAccessException e) {
            throw new NoSuchElementException(
                String.format("No image with ID %d found.", id));
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> edit(@PathVariable final Integer id,
                                     @RequestBody final ImageDto imageDto) {
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
