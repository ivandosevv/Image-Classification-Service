package com.service.classification.image;

import com.service.classification.image.data.Connection;
import com.service.classification.image.data.Image;
import com.service.classification.image.data.Tag;
import com.service.classification.image.repository.UserRepository;
import com.service.classification.image.service.ImageService;
import com.service.classification.image.service.impl.JpaImageServiceImpl;
import com.service.classification.image.repository.jpa.JpaImageRepository;
import org.junit.jupiter.api.Assertions;
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
public class ImageServiceTests {
    @Mock
    private JpaImageRepository repository;

	@Mock
	private UserRepository userRepository;

    private ImageService service;

    @BeforeEach
    void setUp() {
        this.service = new JpaImageServiceImpl(this.repository, this.userRepository);
    }

    @Test
    void testGetAllImagesReturnsProperSize() {
        int allSize = 1;
        List<Image> dummyList = new ArrayList<>();
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        dummyList.add(from);

        when(service.getAllImages()).thenReturn(dummyList);

        assertEquals(dummyList.size(), service.getAllImages().size());
    }

    @Test
    void testGetImageByIdReturnsCorrectImage() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        int id = 1;

        when(repository.getImageById(id)).thenReturn(Optional.of(from));

        Assertions.assertEquals(from, service.getImageById(id));
        verify(repository, times(1)).getImageById(id);
    }

    @Test
    void testGetImageByUrlReturnsCorrectImage() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        String url = "example";

        when(repository.getImageByUrl(url)).thenReturn(Optional.of(from));

        Assertions.assertEquals(from, service.getImageByURL(url));
        verify(repository, times(1)).getImageByUrl(url);
    }

    @Test
    void testGetImageWithValidSetReturnsCorrectImages() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        int firstIndex = 0;
        Set<Integer> imageSet = new HashSet<>();
        List<Image> result = new ArrayList<>();
        result.add(from);
        imageSet.add(from.getId());

        when(repository.findAllById(imageSet)).thenReturn(result);

        Assertions.assertEquals(from, service.get(imageSet).get(firstIndex));
        verify(repository, times(1)).findAllById(imageSet);
    }

    @Test
    void testAddImageWithValidParametersChecksIfItExists() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);

        when (this.repository.getImageByUrl(from.getUrl())).thenReturn(Optional.of(new Image()));

        assertThrows(IllegalArgumentException.class, () -> this.service.addImage(from));
        verify(this.repository, times(1)).getImageByUrl(from.getUrl());
    }

    @Test
    void testUpdateImageWithValidParametersCheckIfTheyAreUpdated() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        Image to = new Image(1, "newExample", new Timestamp(1), 1, 1);

        when(this.repository.getImageById(from.getId())).thenReturn(Optional.of(from));
        this.service.updateImage(from.getId(), to);

        assertEquals(this.service.getAllImages().size(), 0);
    }

    @Test
    void testSaveAllImagesCheckIfTheyAreReturned() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        int firstIndex = 0;
        List<Image> result = new ArrayList<>();
        result.add(from);

        when(repository.saveAll(result)).thenReturn(result);

        assertEquals(result, service.saveAll(result));
    }

    @Test
    void testGetConnectionsByImageIdReturnsValidList() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        Connection toReturn = new Connection(1, 1, from, new Tag(1, "exampleTag"), 100.0);
        List<Connection> result = new ArrayList<>();
        result.add(toReturn);
        from.addConnection(toReturn);

        when(repository.getImageById(from.getId())).thenReturn(Optional.of(from));

        assertEquals(result, service.getConnectionsByImageId(from.getId()));
        assertEquals(from.getConnections().size(), result.size());
    }

    @Test
    void testGetConnectionsByUrlIdReturnsValidList() {
        Image from = new Image(1, "example", new Timestamp(1), 1, 1);
        Connection toReturn = new Connection(1, 1, from, new Tag(1, "exampleTag"), 100.0);
        List<Connection> result = new ArrayList<>();
        result.add(toReturn);
        from.addConnection(toReturn);

        when(repository.getImageByUrl(from.getUrl())).thenReturn(Optional.of(from));

        assertEquals(result, service.getConnectionsByImageURL(from.getUrl()));
    }
}
