package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.BaseReadRepository;

import java.util.List;

public interface UserService extends BaseReadRepository<User> {
//    List<User> getAll();
//
//    User getById(long Id);

    void create(User user);

    void update(User user);

    void delete(long id);
}
