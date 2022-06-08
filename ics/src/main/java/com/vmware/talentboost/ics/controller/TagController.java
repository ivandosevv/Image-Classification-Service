package com.vmware.talentboost.ics.controller;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.dto.ImageDto;
import com.vmware.talentboost.ics.dto.TagDto;
import com.vmware.talentboost.ics.service.ImageService;
import com.vmware.talentboost.ics.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("tags")
@CrossOrigin
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> get() {
        return tagService.getAllTags();
    }

    @GetMapping("{name}")
    public List<Image> get(@PathVariable String name) {
        try {
            List<Connection> connections = tagService.getConnectionsByTagName(name);
            List<Image> result = new ArrayList<>();
            for (Connection connection: connections) {
                result.add(connection.getImage());
            }

            return result;
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(String.format("Tag with name %d not found", name));
        }
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final TagDto tagDto) {
        if (!StringUtils.hasText(tagDto.name)) {
            throw new IllegalArgumentException("Tag name must be specified.");
        }

        final Tag tagToCreate = new Tag();
        System.out.println(tagDto.name);
        tagToCreate.setName(tagDto.name);

        tagService.addTag(tagToCreate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable final Integer id) {
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
