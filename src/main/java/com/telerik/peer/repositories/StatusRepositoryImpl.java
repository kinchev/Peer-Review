package com.telerik.peer.repositories;

import com.telerik.peer.models.Status;
import com.telerik.peer.repositories.contracts.StatusRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StatusRepositoryImpl extends AbstractCRUDRepository<Status> implements StatusRepository {
    SessionFactory sessionFactory;

    @Autowired
    protected StatusRepositoryImpl(SessionFactory sessionFactory) {
        super(Status.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
