package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.services.contracts.ImageService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final UserService userService;
    private final ImageService imageService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserMvcController(UserService userService, ImageService imageService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.imageService = imageService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;


    }

//    @ModelAttribute("isAuthenticated")
//    public boolean populateIsAuthenticated(HttpSession session) {
//        return session.getAttribute("currentUser") != null;
//    }


    @GetMapping
    public String showUserPage() {
        return "user";
    }
}

