package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.mappers.WorkItemMapper;
import com.telerik.peer.services.contracts.CommentService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/workitem")
public class WorkItemMvcController {

    private final WorkItemService workItemService;
    private final WorkItemMapper workItemMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentService commentService;

    @Autowired
    public WorkItemMvcController(WorkItemService workItemService, WorkItemMapper workItemMapper, UserService userService, AuthenticationHelper authenticationHelper, CommentService commentService) {
        this.workItemService = workItemService;
        this.workItemMapper = workItemMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }



}
