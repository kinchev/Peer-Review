package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.UserRepository;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationHelper {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password.";

    private final UserService userService;


    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The requested resource requires authentication.");
        }

        try {
            String username = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            return userService.getByField("username",username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user username.");
        }
    }

    public User tryGetUser(HttpSession session) {
        String currentUser= (String) session.getAttribute("currentUser");

        if (currentUser == null) {
            throw new AuthenticationFailureException("No user logged in.");
        }

        return userService.getByField("username",currentUser);
    }

    public User verifyAuthentication(String username, String password) {
        try {
//            User user = userService.getById(5);
            User user = userService.getByField("username", username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }

}
