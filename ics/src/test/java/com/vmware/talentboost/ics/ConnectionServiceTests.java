package com.vmware.talentboost.ics;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.ConnectionKey;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.repository.jpa.JpaConnectionRepository;
import com.vmware.talentboost.ics.repository.jpa.JpaImageRepository;
import com.vmware.talentboost.ics.service.ConnectionService;
import com.vmware.talentboost.ics.service.ImageService;
import com.vmware.talentboost.ics.service.impl.JpaConnectionServiceImpl;
import com.vmware.talentboost.ics.service.impl.JpaImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTests {
    @Mock
    private JpaConnectionRepository repository;
    private ConnectionService service;

    @BeforeEach
    public void setUp() {
        this.service = new JpaConnectionServiceImpl(this.repository);
    }

    @Test
    public void addConnectionReturnsProperConnection() {
        Connection currConnection = new Connection(1, 1,
            new Image(1, "example", new Timestamp(1), 1, 1), new Tag(1, "exampleTag"), 100.0);

        when(repository.saveAndFlush(currConnection)).thenReturn(currConnection);

        assertEquals(service.addImageTagConnection(currConnection), currConnection);
        assertEquals(currConnection.getId().getImageId(), new ConnectionKey(1, 1).getImageId());
        assertEquals(currConnection.getTag().getName(), new Tag(1, "exampleTag").getName());
        assertEquals(currConnection.getImage().getId(), new Image(1, "example", new Timestamp(1), 1, 1).getId());
        assertEquals(currConnection.getConfidence(), 100.0);
    }
}
