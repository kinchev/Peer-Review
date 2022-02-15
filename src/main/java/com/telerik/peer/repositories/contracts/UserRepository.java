package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CRUDRepository<User> {
    List<User> search(Optional<String> username, Optional<String> email, Optional<String> number);

    User findByEmailId(String emailId);
}
