package com.telerik.peer.services;

import com.telerik.peer.models.Attachment;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.AttachmentRepository;
import com.telerik.peer.services.contracts.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Autowired


    @Override
    public List<Attachment> getAll() {
        return null;
    }

    @Override
    public Attachment getById(long id) {
        return null;
    }

    @Override
    public void delete(long id, User owner) {

    }

    @Override
    public void create(Attachment entity) {

    }

    @Override
    public void update(Attachment entity, User owner) {

    }

    @Override
    public <V> Attachment getByField(String fieldName, V value) {
        return null;
    }
}
