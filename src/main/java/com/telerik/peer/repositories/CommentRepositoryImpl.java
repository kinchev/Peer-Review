package com.telerik.peer.repositories;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl {
    SessionFactory sessionFactory;

    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


}
