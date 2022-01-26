package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.*;
import com.telerik.peer.mappers.TeamMapper;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.AddByIdDto;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/teams")
public class TeamMvcController {

    private final TeamService teamService;
    private final WorkItemService workItemService;
    private final TeamMapper teamMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TeamMvcController(TeamService teamService, WorkItemService workItemService, TeamMapper teamMapper,
                             UserService userService, AuthenticationHelper authenticationHelper) {
        this.teamService = teamService;
        this.workItemService = workItemService;
        this.teamMapper = teamMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("teams")
    public List<Team> populateTeams() {
        return teamService.getAll();
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        return userService.getAll();
    }

    @GetMapping
    public String showAllTeams(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("teams", teamService.getAll());
        return "teams-all";
    }

    @GetMapping("/{id}")
    public String showSingleTeam(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Team team = teamService.getById(id);
            List<WorkItem> activeTeamWorkItems = workItemService.showAllTeamActiveWorkItems(team);
            List<WorkItem> closedTeamWorkItems = workItemService.showAllTeamClosedWorkItems(team);
            model.addAttribute("user", user);
            model.addAttribute("team", team);
            model.addAttribute("activeTeamWorkItems", activeTeamWorkItems);
            model.addAttribute("closedTeamWorkItems", closedTeamWorkItems);

            return "team-single";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewTeamPage(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("teamDto", new TeamDto());
        model.addAttribute("user", user);
        return "team-new";
    }

    @PostMapping("/new")
    public String createTeam(@Valid @ModelAttribute("TeamDto") TeamDto teamDto,
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
            return "team-new";
        }

        try {
            Team team = teamMapper.fromDto(teamDto);
            teamService.create(team);
            return "redirect:/teams";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("teamName", "duplicate_workItem", e.getMessage());
            return "team-new";
        } catch (InvalidUserInputException e) {
            errors.rejectValue("ownerId", "team_mismatch", e.getMessage());
            return "team-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


    @GetMapping("/{id}/update")
    public String showEditTeamPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        try {
            Team team = teamService.getById(id);
            TeamDto teamDto = teamMapper.TeamToDto(team);
            model.addAttribute("teamId", id);
            model.addAttribute("teamDto", teamDto);
            model.addAttribute("team-members", team.getMembers());
            return "team-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateTeam(@PathVariable int id,
                             @Valid @ModelAttribute("team") TeamDto teamDto,
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
            return "team-update";
        }

        try {
            Team team = teamMapper.fromDto(teamDto, id);
            teamService.update(team, user);

            return "redirect:/";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("teamName", "duplicate_team", e.getMessage());
            return "team-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteTeam(@PathVariable long id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }

        try {
            teamService.delete(id, user);
            return "redirect:/teams";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/add")
    public String addMemberToTeam(@PathVariable long id,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("teamId", id);
        model.addAttribute("addDto", new AddByIdDto());
        return "team-add";
    }


    @PostMapping("/{id}/add")
    public String addMemberToTeam(@PathVariable long id, @ModelAttribute("addDto") AddByIdDto dto,
                                       Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Team team = teamService.getById(id);
            User userToAdd = userService.getById(dto.getId());
            teamService.addMemberToTeam(team, user, userToAdd);
            return "redirect:/teams";
        } catch (DuplicateEntityException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/remove")
    public String RemoveMemberFromTeam(@RequestHeader HttpHeaders headers, @PathVariable long id,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("teamId", id);
        model.addAttribute("removeDto", new AddByIdDto());
        return "team-remove";
    }


    @PostMapping("/{id}/remove")
    public String removeMemberFromTeam(@RequestHeader HttpHeaders headers, @PathVariable long id,
                                  @ModelAttribute("removeDto") AddByIdDto dto,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Team team = teamService.getById(id);
            User userToRemove = userService.getById(dto.getId());
            teamService.removeMemberFromTeam(team, user, userToRemove);
            return "redirect:/teams";
        } catch (DuplicateEntityException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


}

//    public String removeMemberFromTeam(@RequestHeader HttpHeaders headers, @PathVariable long id, @PathVariable long userId,
//                                       Model model, HttpSession session) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//        try {
//            Team team = teamService.getById(id);
//            User userToRemove = userService.getById(userId);
//            teamService.removeMemberFromTeam(team, user, userToRemove);
//            return "redirect:/teams" + team.getTeamId();
//        } catch (DuplicateEntityException | UnauthorizedOperationException e) {
//            model.addAttribute("error", e.getMessage());
//            return "access-denied";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "not-found";
//        }
//    }