package com.telerik.peer.repositories;

import com.telerik.peer.models.Comment;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.CommentRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends AbstractCRUDRepository<Comment> implements CommentRepository {
    SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        super(Comment.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

}
