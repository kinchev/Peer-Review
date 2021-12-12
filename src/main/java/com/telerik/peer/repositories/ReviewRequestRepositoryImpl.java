package com.telerik.peer.repositories;

import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.ReviewRequestRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRequestRepositoryImpl extends AbstractCRUDRepository<ReviewRequest> implements ReviewRequestRepository {
    SessionFactory sessionFactory;

    @Autowired
    protected ReviewRequestRepositoryImpl(SessionFactory sessionFactory) {
        super(ReviewRequest.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ReviewRequest> getReviewRequestByCreator(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ReviewRequest where creator.id = :userId", ReviewRequest.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }


    @Override
    public List<ReviewRequest> getReviewRequestByReviewer(long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from ReviewRequest where reviewer.id = :userId", ReviewRequest.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}
