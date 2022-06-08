package com.vmware.talentboost.ics.service.impl;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.repository.jpa.JpaImageRepository;
import com.vmware.talentboost.ics.service.ImageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class JpaImageServiceImpl implements ImageService {
    private JpaImageRepository repository;

    public JpaImageServiceImpl(JpaImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Image> getAllImages() {
        return this.repository.findAll();
    }

    @Override
    public Image getImageByURL(String URL) {
        Optional<Image> image = this.repository.getImageByUrl(URL);
        if (image.isPresent()) {
            return image.get();
        }

        throw new NoSuchElementException("Image with URL: " + URL + " was not found");
    }

    @Override
    public Image getImageById(int id) {
        Optional<Image> image = this.repository.getImageById(id);
        if (image.isPresent()) {
            return image.get();
        }

        throw new NoSuchElementException("Image with ID: " + id + "was not found");
    }

    @Override
    public List<Image> get(Set<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public Image addImage(Image image) {
        if (!this.repository.getImageByUrl(image.getUrl()).isPresent()) {
            return this.repository.saveAndFlush(image);
        }

        //return this.repository.getImageByUrl(image.getUrl()).get();
        throw new IllegalArgumentException("Already exists");
    }

    @Override
    @Transactional
    public Image updateImage(int id, Image image) {
        Optional<Image> myImage = repository.getImageById(id);
        if (myImage.isPresent()) {
            Image dbImage = myImage.get();
            dbImage.setUrl(image.getUrl());
            //dbImage.setService(image.getService());
            dbImage.setAddedOn(image.getAddedOn());
            dbImage.setHeight(image.getHeight());
            dbImage.setWidth(image.getWidth());
            dbImage.setConnections(image.getImageConnections());

            return this.repository.saveAndFlush(dbImage);
        }

        throw new NoSuchElementException("Image with id: " + id + " doesn't exist");
    }

    @Override
    public List<Image> saveAll(List<Image> images) {
        return this.repository.saveAll(images);
    }

    @Override
    public void deleteImage(int id) {
        this.repository.deleteById((int) id);
    }

    @Override
    public List<Connection> getConnectionsByImageId(Integer id) {
        return this.getImageById(id).getImageConnections();
    }

    @Override
    public List<Connection> getConnectionsByImageURL(String URL) {
        return this.getImageByURL(URL).getImageConnections();
    }

    @Override
    public void addConnectionToImage(Connection connection) {
        Image image = new Image();

        try {
            image = getImageById(connection.getImage().getId());
            image.addConnection(connection);
            //image.setTags();
            this.updateImage(image.getId(), image);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("No such image exists");
        }
    }
}