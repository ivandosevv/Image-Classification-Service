package com.service.classification.image.service.impl;

import com.service.classification.image.data.Connection;
import com.service.classification.image.data.Tag;
import com.service.classification.image.repository.jpa.JpaTagRepository;
import com.service.classification.image.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class JpaTagServiceImpl implements TagService {
    private final JpaTagRepository repository;
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaTagServiceImpl.class);

    public JpaTagServiceImpl(JpaTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tag> getAllTags() {
        return this.repository.findAll();
    }

    @Override
    public Tag getTagByName(String name) {
        Optional<Tag> tag = this.repository.getTagByName(name);
        if (tag.isPresent()) {
            return tag.get();
        }

        throw new NoSuchElementException("Image with name: " + name + "was not found");
    }

    @Override
    public Tag getTagById(int id) {
        Optional<Tag> tag = this.repository.getTagById(id);
        if (tag.isPresent()) {
            return tag.get();
        }

        throw new NoSuchElementException("Tag with ID: " + id + "was not found");
    }

    @Override
    public List<Tag> get(Set<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public Tag addTag(Tag tag) {
        if (!this.repository.getTagByName(tag.getName()).isPresent()) {
            return this.repository.saveAndFlush(tag);
        }

        return this.repository.getTagByName(tag.getName()).get();
    }

    @Override
    @Transactional
    public Tag updateTag(int id, Tag tag) {
        Optional<Tag> myTag = repository.getTagById(id);
        if (myTag.isPresent()) {
            Tag dbTag = myTag.get();
            dbTag.setName(tag.getName());
            dbTag.setConnections(tag.getConnections());
            return repository.saveAndFlush(dbTag);
        }

        throw new NoSuchElementException("Tag with id: " + id + " doesn't exist");
    }

    @Override
    public List<Tag> saveAll(List<Tag> tags) {
        return this.repository.saveAll(tags);
    }

    @Override
    public void deleteTag(int id) {
        this.repository.deleteById((int) id);
    }

    @Override
    public List<Connection> getConnectionsByTagId(Integer id) {
        return this.getTagById(id).getConnections();
    }

    @Override
    public List<Connection> getConnectionsByTagName(String name) {
        return this.getTagByName(name).getConnections();
    }

    @Override
    public void addConnectionToTag(Connection connection) {
        Tag tag = new Tag();

        try {
            tag = getTagById(connection.getTag().getId());
            tag.addConnection(connection);
            this.updateTag(tag.getId(), tag);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("No such name exists");
        }
    }
}
