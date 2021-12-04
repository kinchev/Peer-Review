package com.telerik.peer.services;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.UserRepository;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private static final String MODIFY_USER_ERROR_MESSAGE = "Only the user owner or admin can modify an user.";
    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public void delete(long id, User owner) {
        User userToDelete = userRepository.getById(id);
        if (!owner.equals(userToDelete)) {
            throw new UnauthorizedOperationException(MODIFY_USER_ERROR_MESSAGE);
        }
        userRepository.delete(id);
    }

    @Override
    public void create(User entity) {
        boolean duplicateExists = true;
        try {
            String encodedPassword=this.passwordEncoder.encode(entity.getPassword());
            entity.setPassword(encodedPassword);
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
            User exisingUser = userRepository.getByField("username", entity.getUsername());
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
    public <V> User getByField(String fieldName, V value) {
        return userRepository.getByField(fieldName, value);
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
