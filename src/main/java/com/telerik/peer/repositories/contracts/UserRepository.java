package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends BaseCRUDRepository<User>,BaseReadRepository<User> {
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
