package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.*;
import com.telerik.peer.mappers.WorkItemMapper;
import com.telerik.peer.models.Status;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.*;
import com.telerik.peer.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/workItems")
public class WorkItemMvcController {

    private final WorkItemService workItemService;
    private final WorkItemMapper workItemMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TeamService teamService;
    private final CommentService commentService;
    private final AttachmentService attachmentService;
    private final StatusService statusService;

    @Autowired
    public WorkItemMvcController(WorkItemService workItemService, WorkItemMapper workItemMapper, UserService userService, AuthenticationHelper authenticationHelper, TeamService teamService, CommentService commentService, AttachmentService attachmentService, StatusService statusService) {
        this.workItemService = workItemService;
        this.workItemMapper = workItemMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.teamService = teamService;
        this.commentService = commentService;
        this.attachmentService = attachmentService;
        this.statusService = statusService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("statuses")
    public List<Status> populateStatuses() {
        return statusService.getAll();
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        return userService.getAll();
    }

    @ModelAttribute("teams")
    public List<Team> populateTeams() {
        return teamService.getAll();
    }



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
        model.addAttribute("workItemDto", new WorkItemUpdateDto());


        return "workitems-all";
    }

    @GetMapping("/created")
    public String showCreatedWorkItems(HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        model.addAttribute("workItemsCreated", workItemService.getByField("creator", user.getUsername()));
        model.addAttribute("user", user);
        return "workItems";
    }

    @GetMapping("/reviewed")
    public String showReviewedWorkItems(HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("workItemsReviewed", workItemService.getByField("reviewer", user.getUsername()));
        model.addAttribute("user", user);
        return "workItems";
    }


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
            return "workitem-single";
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
            return "redirect:/auth/login";
        }
        model.addAttribute("workItemDto", new WorkItemDto());
        model.addAttribute("user", user);
        return "workitem-new";
    }

    @PostMapping("/new")
    public String createWorkItem(@Valid @ModelAttribute("workItemDto") WorkItemDto workItemDto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session,
                                 @RequestParam("file") MultipartFile multipartFile) throws IOException {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "workitem-new";
        }

        try {
            WorkItem workItem = workItemMapper.fromDto(workItemDto);
            workItemService.create(workItem);
            attachmentService.create(user, workItem, multipartFile);
            return "redirect:/workItems";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("title", "duplicate_workItem", e.getMessage());
            return "workitem-new";
        } catch (InvalidUserInputException e) {
            errors.rejectValue("creatorId", "team_mismatch", e.getMessage());
            return "workitem-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWorkItemPage(@PathVariable long id, Model model, HttpSession session) {
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
            model.addAttribute("workItemUpdateDto", dto);
            model.addAttribute("user", user);

            return "workitem-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWorkitem(@PathVariable long id,
                                 @Valid @ModelAttribute("workItemUpdateDto") WorkItemUpdateDto dto,
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
            return "access-denied";
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

    @GetMapping("/{id}/status")
    public String setStatus(@RequestHeader HttpHeaders headers, @PathVariable long id,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("workItemId", id);
        model.addAttribute("statusDto", new StatusDto());
        return "workitem-status";
    }

    @PostMapping("/{id}/status")
    public String setStatusWithComment(@PathVariable long id, @ModelAttribute("statusDto") StatusDto dto,
                                         Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            WorkItem workItem = workItemService.getById(id);
            Status status = statusService.getById(dto.getId());
            String commentToAdd = dto.getComment();
            workItemService.setStatus(workItem, user, status, commentToAdd);
            return "redirect:/workItems";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}

