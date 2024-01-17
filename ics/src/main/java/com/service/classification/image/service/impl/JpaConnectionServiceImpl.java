package com.service.classification.image.service.impl;

import com.service.classification.image.data.Connection;
import com.service.classification.image.repository.jpa.JpaConnectionRepository;
import com.service.classification.image.service.ConnectionService;
import org.springframework.stereotype.Service;

@Service
public class JpaConnectionServiceImpl implements ConnectionService {
    private JpaConnectionRepository connectionRepository;

    public JpaConnectionServiceImpl(JpaConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }


    @Override
    public Connection addImageTagConnection(Connection connection) {
        return this.connectionRepository.saveAndFlush(connection);
    }
}
