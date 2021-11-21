package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpHeaders;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    //    private final UserMapper userMapper;
    private final WorkItem workItem;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService, WorkItem workItem, AuthenticationHelper authenticationHelper) {
        this.userService = userService;

        this.workItem = workItem;
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
}
