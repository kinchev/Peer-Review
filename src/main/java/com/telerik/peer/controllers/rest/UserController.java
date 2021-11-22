package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.User;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpHeaders;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    private final AuthenticationHelper authenticationHelper;

    public UserController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping("{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByField("id", user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }
}
