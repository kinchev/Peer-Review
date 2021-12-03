package com.telerik.peer.services;

import com.telerik.peer.models.Status;
import com.telerik.peer.repositories.contracts.StatusRepository;
import com.telerik.peer.services.contracts.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Status getById(long id) {
        return statusRepository.getById(id);
    }

    @Override
    public <V> Status getByField(String fieldName, V value) {
        return statusRepository.getByField(fieldName, value);
    }
}
