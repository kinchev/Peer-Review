package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends CRUDService<User>  {

    List<User> search(Optional<String> username, Optional<String> email, Optional<String> number);

//
//    List<User> getAll();
//
//    User getById(long Id);
//
//    //
//    void update(User user, User owner);
//
//    //
//    void delete(long id, User owner);
//
//    void create(User user);
//
//    <V> User getByField(String fieldName, V value);
}
