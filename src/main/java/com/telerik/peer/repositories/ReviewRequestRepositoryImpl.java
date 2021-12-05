package com.telerik.peer.repositories;

import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.repositories.contracts.ReviewRequestRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRequestRepositoryImpl extends AbstractCRUDRepository<ReviewRequest> implements ReviewRequestRepository {
    SessionFactory sessionFactory;

    @Autowired
    protected ReviewRequestRepositoryImpl(SessionFactory sessionFactory) {
        super(ReviewRequest.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

}
