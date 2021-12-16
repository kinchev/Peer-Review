package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.services.contracts.ReviewRequestService;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final UserService userService;
    private final WorkItemService workItemService;
    private final TeamService teamService;
    private final ReviewRequestService reviewRequestService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserMvcController(UserService userService, WorkItemService workItemService,
                             TeamService teamService, ReviewRequestService reviewRequestService,
                             UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.workItemService = workItemService;
        this.teamService = teamService;
        this.reviewRequestService = reviewRequestService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    public String showUserPersonalPage(Model model,
                                       HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
        return "user";
    }


    @GetMapping
    public String showAllUsers(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", user);
        return "user";
    }

        @GetMapping("/{id}")
        public String showSingleUser ( @PathVariable int id, Model model){
            try {
                User user = userService.getById(id);
                model.addAttribute("user", user);
                return "user";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "not-found";
            }
        }

        @GetMapping("/{id}/update")
        public String showEditUserPage ( @PathVariable int id, Model model, HttpSession session){
            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationFailureException e) {
                return "redirect:/auth/login";
            }
            try {
                UserDto userDto = userMapper.userToDto(user);
                model.addAttribute("userId", id);
//                model.addAttribute("userDto", userDto);
                model.addAttribute("user", user);
                return "user-update";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "not-found";
            }
        }

        @PostMapping("/{id}/update")
        public String updateUser ( @PathVariable long id,
        @Valid @ModelAttribute("user") User user,
        BindingResult errors,
        Model model,
        HttpSession session){
            User userToUpdate = userService.getById(id);
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationFailureException e) {
                return "redirect:/auth/login";
            }
            if (errors.hasErrors()) {
                return "user-update";
            }
            try {
                userService.update(userToUpdate, user);
                return "user-update";
            } catch (DuplicateEntityException e) {
                errors.rejectValue("name", "duplicate_user", e.getMessage());
                return "user-update";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "not-found";
            }
        }

        @GetMapping("/{id}/delete")
        public String deleteUser ( @PathVariable long id, Model model){
            try {
                User user = userService.getById(id);
                userService.delete(id, user);

                return "user-delete";
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "not-found";
            }
        }

        @GetMapping("/user")
        public String showAllUsers (Model model){
            model.addAttribute("users", userService.getAll());
            return "user";
        }


    }

