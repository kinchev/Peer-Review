package com.telerik.peer.controllers.rest;

import com.telerik.peer.models.WorkItem;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final WorkItem workItem;

    public UserController(UserService userService, UserMapper userMapper, WorkItem workItem) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.workItem = workItem;
    }

}
