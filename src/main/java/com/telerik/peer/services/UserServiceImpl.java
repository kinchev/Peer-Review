package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.AbstractCRUDRepository;
import com.telerik.peer.repositories.contracts.UserRepository;
import com.telerik.peer.services.contracts.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends AbstractCRUDRepository<User> implements UserService {
    private static final String MODIFY_USER_ERROR_MESSAGE = "Only the user owner or admin can modify an user.";
    private final UserRepository userRepository;
    private final WorkItem workItem;

    @Autowired
    public UserServiceImpl(Class<User> clazz, SessionFactory sessionFactory, UserRepository userRepository, WorkItem workItem) {
        super(clazz, sessionFactory);
        this.userRepository = userRepository;
        this.workItem = workItem;
    }


    @Override
    public void delete(long id, User owner) {
        User userToDelete = userRepository.getById(id);
        if (!owner.equals(userToDelete)){
            throw new UnauthorizedOperationException(MODIFY_USER_ERROR_MESSAGE);
        }
        userRepository.delete(id);
    }

    @Override
    public void create(User entity) {
        boolean duplicateExists = true;
        try {
            userRepository.getByField("username", entity.getUsername());

        } catch (EntityNotFoundException e) {
            duplicateExists = false;

        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", entity.getUsername());
        }
        userRepository.create(entity);
    }


    @Override
    public void update(User entity, User owner) {
        if (!owner.equals(entity)) {
            throw new UnauthorizedOperationException(MODIFY_USER_ERROR_MESSAGE);
        }
        boolean duplicateExists = true;
        try {
            User exisingUser = getByField("username", entity.getUsername());
            if (exisingUser.getId() == entity.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", entity.getUsername());
        }
        userRepository.update(entity);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }


}