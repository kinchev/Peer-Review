package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CRUDRepository<User> {
    List<User> search(Optional<String> username, Optional<String> email, Optional<String> number);

//    List<User> getAll();
//
//    User getById(long Id);
//
//    void create(User user);
//
//    void update(User user);
//
//    void delete(long id);
}
