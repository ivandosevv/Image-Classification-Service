package com.vmware.talentboost.ics.service.impl;

import com.vmware.talentboost.ics.data.Connection;
import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.repository.jpa.JpaConnectionRepository;
import com.vmware.talentboost.ics.repository.jpa.JpaImageRepository;
import com.vmware.talentboost.ics.repository.jpa.JpaTagRepository;
import com.vmware.talentboost.ics.service.ConnectionService;
import org.springframework.stereotype.Service;

@Service
public class JpaConnectionServiceImpl implements ConnectionService {
    //private JpaImageRepository imageRepository;
    //private JpaTagRepository tagRepository;
    private JpaConnectionRepository connectionRepository;

    public JpaConnectionServiceImpl(JpaConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }


    @Override
    public Connection addImageTagConnection(Connection connection) {
        return this.connectionRepository.saveAndFlush(connection);
//
//        if (this.connectionRepository.getConnectionByImageId(connection.getId().getImageId()).isEmpty()
//            && this.connectionRepository.getConnectionByTagId(connection.getId().getTagId()).isEmpty()) {
//            return this.connectionRepository.saveAndFlush(connection);
//        }
//
//        return this.connectionRepository.get(connection.getId().getImageId());
    }
}