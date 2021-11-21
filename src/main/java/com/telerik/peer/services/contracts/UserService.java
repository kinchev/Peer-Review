package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.BaseCRUDRepository;
import com.telerik.peer.repositories.contracts.BaseReadRepository;

import java.util.List;

public interface UserService extends  BaseCRUDRepository<User> {


    //    List<User> getAll();
//
//    User getById(long Id);
//
    void update(User user,User owner);
//
//    void update(User user);
//
//    void delete(long id);
}
