package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.LoginDto;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.utils.FileUploadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;


    @Autowired
    public AuthenticationController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;


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
//            if (user.isAdmin()) {
//
//                return "redirect:/admin";
//            } else {
            return "user2";
//            }
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
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

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult,
                                 @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "register";
        }
        User user;
        String fileName;
        try {
            user = userMapper.createUserFromRegisterDto(register);
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotoName(fileName);
            userService.create(user);

        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "register";
        }

        String uploadDir = "src/main/resources/user-photos/" + user.getId();
        FileUploadHelper.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/auth/login";
    }


}