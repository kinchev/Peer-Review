package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.TeamMapper;
import com.telerik.peer.models.Team;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.TeamDto;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public TeamController(TeamService teamService, TeamMapper teamMapper,
                          AuthenticationHelper authenticationHelper, UserService userService) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping
    public List<Team> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return teamService.getAll();
    }

    @GetMapping("{id}")
    public Team getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return teamService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/teamName")
    public Team getByTeamName(@RequestHeader HttpHeaders headers, @RequestParam String teamName) {
        try {
            authenticationHelper.tryGetUser(headers);
            return teamService.getByField("teamName", teamName);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("{id}/members-list")
    public List<User> getMembersList(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        authenticationHelper.tryGetUser(headers);
        return new ArrayList<>(teamService.getById(id).getMembers());
    }

    @PostMapping
    public Team create(@RequestHeader HttpHeaders headers, @Valid @RequestBody TeamDto teamDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Team team = teamMapper.fromDto(teamDto);
            teamService.create(team);
            return team;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Team update(@RequestHeader HttpHeaders headers, @PathVariable long id, @Valid @RequestBody TeamDto teamDto) {
        try {
            Team team = teamMapper.fromDto(teamDto, id);
            User updatingUser = authenticationHelper.tryGetUser(headers);
            teamService.update(team, updatingUser);
            return team;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User deletingUser = authenticationHelper.tryGetUser(headers);
            teamService.delete(id, deletingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/add/{userId}")
    public Team addMemberToTeam(@RequestHeader HttpHeaders headers, @PathVariable long id, @PathVariable long userId) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            Team team = teamService.getById(id);
            User userToAdd = userService.getById(userId);
            teamService.addMemberToTeam(team, updatingUser, userToAdd);
            return team;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/remove/{userId}")
    public Team removeMemberFromTeam(@RequestHeader HttpHeaders headers, @PathVariable long id, @PathVariable long userId) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            Team team = teamService.getById(id);
            User userToAdd = userService.getById(userId);
            teamService.removeMemberFromTeam(team, updatingUser, userToAdd);
            return team;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}


