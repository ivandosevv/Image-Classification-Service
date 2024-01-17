package com.service.classification.image.repository.jpa;

import com.service.classification.image.data.Connection;
import com.service.classification.image.data.ConnectionKey;
import com.service.classification.image.data.Image;
import com.service.classification.image.data.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaConnectionRepository extends JpaRepository<Connection, ConnectionKey> {
    Optional<List<Tag>> getConnectionByImageId(Integer imageId);
    Optional<List<Image>> getConnectionByTagId(Integer tagId);
    Optional<Connection> getConnectionById(Integer id);
    //Optional<Connection> getConnectionByConnectionKey(ConnectionKey connectionKey);
}