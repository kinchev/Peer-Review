package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.User;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserHomeMvcController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserHomeMvcController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }


//    @ModelAttribute("isAuthenticated")
//    public boolean populateIsAuthenticated(HttpSession session) {
//        return session.getAttribute("currentUser") != null;
//    }

    @GetMapping
    public String showSingleUser( Model model,HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "user2";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

//    @GetMapping
//    public String showAllUsers(Model model, HttpSession session) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/login";
//        }
//        model.addAttribute("users", userService.getAll());
//        return "user2";
//    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        return userService.getAll();
    }



}