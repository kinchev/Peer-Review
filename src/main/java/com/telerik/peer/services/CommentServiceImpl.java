package com.telerik.peer.services;

import com.telerik.peer.models.Comment;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.CommentRepository;
import com.telerik.peer.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public Comment getById(long id) {
        return commentRepository.getById(id);
    }

    @Override
    public void delete(long id, User owner) {
        commentRepository.delete(id);
    }

    @Override
    public void create(Comment entity) {
        commentRepository.create(entity);
    }

    @Override
    public void update(Comment entity, User owner) {
        commentRepository.update(entity);
    }

    @Override
    public <V> Comment getByField(String fieldName, V value) {
        return null;
    }
}
