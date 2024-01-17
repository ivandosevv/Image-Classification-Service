package com.service.classification.image.service;

import com.service.classification.image.data.Connection;
import com.service.classification.image.data.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTagByName(String name);

    Tag getTagById(int id);

    List<Tag> get(Set<Integer> ids);

    Tag addTag(Tag tag);

    Tag updateTag(int id, Tag tag);

    //Tag assignImage(int tagId, int imageId);

    List<Tag> saveAll(List<Tag> tags);

    void deleteTag(int id);

    List<Connection> getConnectionsByTagId(Integer id);
    List<Connection> getConnectionsByTagName(String name);
    void addConnectionToTag(Connection connection);
}
