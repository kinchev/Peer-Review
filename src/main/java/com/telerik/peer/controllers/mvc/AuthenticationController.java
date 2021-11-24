package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.Image;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.LoginDto;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.services.contracts.ImageService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final ImageService imageService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper, ImageService imageService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.imageService = imageService;
    }

    public boolean isAdmin() {
        return true;
    }

    //да Покаже login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    //грижи се да се login in...HttpSession
    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            User user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());
            if (user.isAdmin()) {

                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }

    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }

//    @PostMapping("/register")
//    public Object handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
//                                 BindingResult bindingResult,
//                                 HttpSession session,@RequestParam MultipartFile multipartFile) {
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//
//        if (!register.getPassword().equals(register.getPasswordConfirm())) {
//            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
//            return "register";
//        }
//
//        try {
//            User user = userMapper.createUserFromRegisterDto(register);
//            Image image = userMapper.createImageFromRegisterDto(register);
//
//
//        } catch (DuplicateEntityException e) {
//            bindingResult.rejectValue("username", "username_error", e.getMessage());
//            return "register";
//
//        }


    }