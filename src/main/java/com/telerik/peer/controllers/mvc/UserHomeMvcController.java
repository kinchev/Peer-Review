package com.telerik.peer.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserHomeMvcController {
//    private final UserService userService;
//
//    public HomeMvcController(UserService userService) {
//        this.userService = userService;
//    }


    //
//    @ModelAttribute("isAuthenticated")
//    public boolean populateIsAuthenticated(HttpSession session) {
//        return session.getAttribute("currentUser") != null;
//    }
    @GetMapping
    public String homePageUser(Model model) {
        return "user2";
    }
}