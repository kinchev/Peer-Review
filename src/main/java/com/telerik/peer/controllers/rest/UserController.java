package com.telerik.peer.controllers.rest;

import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

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
@GetMapping("{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try{
            User user=
        }
}
}
