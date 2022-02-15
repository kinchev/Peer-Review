package com.telerik.peer.services.contracts;

import com.telerik.peer.models.User;

public interface VerificationTokenService {
    void createVerificationToken(User user, String token);

    void deleteVerificationToken(User user);

    User findUserByToken(String token);
}
