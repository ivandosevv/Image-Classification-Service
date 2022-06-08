package com.vmware.talentboost.ics;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.repository.jpa.JpaTagRepository;
import com.vmware.talentboost.ics.service.TagService;
import com.vmware.talentboost.ics.service.impl.JpaTagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTests {
    @Mock
    private JpaTagRepository repository;
    private TagService service;

    @BeforeEach
    void setUp() {
        this.service = new JpaTagServiceImpl(this.repository);
    }

    @Test
    void testGetAllTagsReturnsProperSize() {
        int allSize = 1;
        List<Tag> dummyList = new ArrayList<>();
        Tag from = new Tag(1, "example");
        dummyList.add(from);

        when(service.getAllTags()).thenReturn(dummyList);

        assertEquals(dummyList.size(), service.getAllTags().size());
    }

    @Test
    void testGetTagByIdReturnsProperTag() {
        Tag from = new Tag(1, "example");
        int id = 1;

        when(repository.getTagById(id)).thenReturn(Optional.of(from));

        assertEquals(from, service.getTagById(id));
        verify(repository, times(1)).getTagById(id);
    }

    @Test
    void testGetTagByNameReturnsProperTag() {
        Tag from = new Tag(1, "example");
        String name = "example";

        when(repository.getTagByName(name)).thenReturn(Optional.of(from));

        assertEquals(from, service.getTagByName(name));
        verify(repository, times(1)).getTagByName(name);
    }

    @Test
    void testGetTagWithValidSetReturnsCorrectImages() {
        Tag from = new Tag(1, "example");
        int firstIndex = 0;
        Set<Integer> tagSet = new HashSet<>();
        List<Tag> result = new ArrayList<>();
        result.add(from);
        tagSet.add(from.getId());

        when(repository.findAllById(tagSet)).thenReturn(result);

        assertEquals(from, service.get(tagSet).get(firstIndex));
        verify(repository, times(1)).findAllById(tagSet);
    }

    @Test
    void testAddTagWithValidParametersChecksIfItExists() {
        Tag from = new Tag(1, "example");

        when (this.repository.getTagByName(from.getName())).thenReturn(Optional.of(from));

        assertEquals(this.service.addTag(from), from);
    }

    @Test
    void testUpdateTagWithValidParametersCheckIfTheyAreUpdated() {
        Tag from = new Tag(1, "example");
        Tag to = new Tag(1, "new");

        when(this.repository.getTagById(from.getId())).thenReturn(Optional.of(from));
        this.service.updateTag(from.getId(), to);

        assertEquals(this.service.getAllTags().size(), 0);
    }

    @Test
    void testSaveAllTagsCheckIfTheyAreReturned() {
        Tag from = new Tag(1, "example");
        int firstIndex = 0;
        List<Tag> result = new ArrayList<>();
        result.add(from);

        when(repository.saveAll(result)).thenReturn(result);

        assertEquals(result, service.saveAll(result));
    }

    @Test
    void testGetConnectionsByTagIdReturnsValidList() {
        Tag from = new Tag(1, "example");
        Image testImage = new Image(1, "example", new Timestamp(1), 1, 1);
        Connection toReturn = new Connection(1, 1, testImage, from, 100.0);
        List<Connection> result = new ArrayList<>();
        result.add(toReturn);
        from.addConnection(toReturn);

        when(repository.getTagById(from.getId())).thenReturn(Optional.of(from));

        assertEquals(result, service.getConnectionsByTagId(from.getId()));
        assertEquals(from.getConnections().size(), result.size());
    }

    @Test
    void testGetConnectionsByUrlIdReturnsValidList() {
        Tag from = new Tag(1, "example");
        Image testImage = new Image(1, "example", new Timestamp(1), 1, 1);
        Connection toReturn = new Connection(1, 1, testImage, from, 100.0);
        List<Connection> result = new ArrayList<>();
        result.add(toReturn);
        from.addConnection(toReturn);

        when(repository.getTagByName(from.getName())).thenReturn(Optional.of(from));

        assertEquals(result, service.getConnectionsByTagName(from.getName()));
    }
}
