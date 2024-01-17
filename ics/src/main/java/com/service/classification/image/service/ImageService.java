package com.service.classification.image.service;

import com.service.classification.image.data.Connection;
import com.service.classification.image.data.Image;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ImageService {
    List<Image> getAllImages();

    Image getImageByURL(String URL);

	List<Image> getImagesByUser(String username);

    Image getImageById(int id);

    List<Image> get(Set<Integer> ids);

	CompletableFuture<Image> addImage(Image image);

    Image updateImage(int id, Image image);

    //Image assignTag(int imageId, int tagId);

    List<Image> saveAll(List<Image> images);

    void deleteImage(int id);

    List<Connection> getConnectionsByImageId(Integer id);

    List<Connection> getConnectionsByImageURL(String URL);

    void addConnectionToImage(Connection connection);

}
