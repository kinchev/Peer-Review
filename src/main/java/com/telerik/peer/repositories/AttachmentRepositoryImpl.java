package com.telerik.peer.repositories;

import com.telerik.peer.models.Attachment;
import com.telerik.peer.repositories.contracts.AttachmentRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentRepositoryImpl extends AbstractCRUDRepository<Attachment> implements AttachmentRepository {

    SessionFactory sessionFactory;

    @Autowired
    protected AttachmentRepositoryImpl(SessionFactory sessionFactory) {
        super(Attachment.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
