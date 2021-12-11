package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends CRUDService<User> {

    List<User> search(Optional<String> username, Optional<String> email, Optional<String> number);

    void changePassword(long userId, String oldPassword, String newPassword, String passwordConfirm);
}



