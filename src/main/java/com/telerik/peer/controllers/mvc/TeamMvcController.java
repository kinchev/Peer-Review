package com.telerik.peer.controllers.mvc;

import com.telerik.peer.controllers.rest.AuthenticationHelper;
import com.telerik.peer.exceptions.AuthenticationFailureException;
import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.TeamMapper;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/team")
public class TeamMvcController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TeamMvcController(TeamService teamService, TeamMapper teamMapper,
                             UserService userService, AuthenticationHelper authenticationHelper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("teams")
    public List<Team> populateUsers() {
        return  teamService.getAll();
    }

    @GetMapping
    public String showAllTeams(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        model.addAttribute("teams", teamService.getAll());
        return "user";
    }

    @GetMapping("/{id}")
    public String showSingleTeam(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/login";
        }
        try {
            Team team = teamService.getById(id);
            model.addAttribute("team", team);
            return "user";
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
            model.addAttribute("team", team);
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
            return "redirect:/login";
        }

        if (errors.hasErrors()) {
            return "team-update";
        }

        try {
            Team team = teamMapper.fromDto(teamDto, id);
            teamService.update(team, user);

            return "redirect:/";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_user", e.getMessage());
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
            userService.delete(id, user);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

}
