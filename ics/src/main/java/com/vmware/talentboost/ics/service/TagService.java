package com.vmware.talentboost.ics.service;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;

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
