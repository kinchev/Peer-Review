package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.*;
import com.telerik.peer.mappers.WorkItemMapper;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.models.dto.WorkItemUpdateDto;
import com.telerik.peer.services.contracts.CommentService;
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

@Controller
@RequestMapping("/workItems")
public class WorkItemMvcController {

    private final WorkItemService workItemService;
    private final WorkItemMapper workItemMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TeamService teamService;
    private final CommentService commentService;

    @Autowired
    public WorkItemMvcController(WorkItemService workItemService, WorkItemMapper workItemMapper, UserService userService, AuthenticationHelper authenticationHelper, TeamService teamService, CommentService commentService) {
        this.workItemService = workItemService;
        this.workItemMapper = workItemMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.teamService = teamService;
        this.commentService = commentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        return userService.getAll();
    }

//    @ModelAttribute("teams")
//    public List<Team> populateTeams() {
//        return teamService.getAll();
//    }


    @GetMapping
    public String showAllWorkItems(HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("workItems", workItemService.getAll());
        model.addAttribute("user", user);
        model.addAttribute("workItemDto",new WorkItemUpdateDto());


        return "workitems-all";
    }

//    @GetMapping("/created")
//    public String showCreatedWorkItems(HttpSession session, Model model) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/login";
//        }
//        model.addAttribute("workItemsCreated", workItemService.getByField("creator", user.getUsername()));
//        model.addAttribute("user", user);
//        return "workItems";
//    }
//
//    @GetMapping("/reviewed")
//    public String showReviewedWorkItems(HttpSession session, Model model) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//        model.addAttribute("workItemsReviewed", workItemService.getByField("reviewer", user.getUsername()));
//        model.addAttribute("user", user);
//        return "workItems";
//    }


    @GetMapping("/{id}")
    public String showSingleWorkItem(@PathVariable long id, Model model,
                                     HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            WorkItem workItem = workItemService.getById(id);
            model.addAttribute("workItem", workItem);

            model.addAttribute("user", user);
            return "workitems-all";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());

            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewWorkItemPage(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:login";
        }
        model.addAttribute("workItem", new WorkItemDto());
        model.addAttribute("user", user);
        return "workItem-new";
    }

    @PostMapping("/new")
    public String createWorkItem(@Valid @ModelAttribute("workItem") WorkItemDto workItemDto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "workItem-new";
        }

        try {
            WorkItem workItem = workItemMapper.fromDto(workItemDto);
            workItemService.create(workItem);
            return "redirect:/workItems";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("title", "duplicate_workItem", e.getMessage());
            return "workItem-new";
        } catch (InvalidUserInputException e) {
            errors.rejectValue("creatorId", "team_mismatch", e.getMessage());
            return "workItem-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWorkitemPage(@PathVariable long id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            WorkItem workItem = workItemService.getById(id);
            WorkItemUpdateDto dto = workItemMapper.workItemToUpdateDto(workItem);
            model.addAttribute("workItemId", id);
            model.addAttribute("workItem", dto);

            model.addAttribute("user", user);

            return "workitems-all";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWorkitem(@PathVariable long id,
                                 @Valid @ModelAttribute("workItem") WorkItemUpdateDto dto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "workitems-all";
        }

        try {
            WorkItem workItem = workItemMapper.fromDto(dto, id);
            workItemService.update(workItem, user);
            return "redirect:/workItems";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("workItemTitle", "duplicate-workItem", e.getMessage());
            return "workitems-all";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteWorkitem(@PathVariable long id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            workItemService.delete(id, user);
            return "redirect:/workItems";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

}

