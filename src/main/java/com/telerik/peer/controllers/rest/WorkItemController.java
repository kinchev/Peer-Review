package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.WorkItemMapper;
import com.telerik.peer.models.Comment;
import com.telerik.peer.models.Status;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workitems")
public class WorkItemController {
    AuthenticationHelper authenticationHelper;
    WorkItemService workItemService;
    TeamService teamService;
    CommentService commentService;
    UserService userService;
    StatusService statusService;
    WorkItemMapper workItemMapper;

    @Autowired
    public WorkItemController(AuthenticationHelper authenticationHelper,
                              WorkItemService workItemService, TeamService teamService,
                              CommentService commentService, UserService userService,
                              WorkItemMapper workItemMapper, StatusService statusService) {
        this.authenticationHelper = authenticationHelper;
        this.workItemService = workItemService;
        this.teamService = teamService;
        this.commentService = commentService;
        this.userService = userService;
        this.workItemMapper = workItemMapper;
        this.statusService = statusService;
    }


    @GetMapping
    public List<WorkItem> getAll(@RequestHeader HttpHeaders headers) {

        authenticationHelper.tryGetUser(headers);
        return workItemService.getAll();

    }

    @GetMapping("{id}")
    public WorkItem getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return workItemService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping

    public WorkItem create(@RequestHeader HttpHeaders headers, @Valid @RequestBody WorkItemDto workItemDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            WorkItem workItem = workItemMapper.fromDto(workItemDto);
            workItemService.create(workItem);
            return workItem;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }
    }

    @PutMapping("/{id}")
    public WorkItem update(@RequestHeader HttpHeaders headers, @PathVariable long id, @Valid @RequestBody WorkItemDto workItemDto) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            WorkItem workItem = workItemMapper.fromDto(workItemDto, id);
            workItemService.update(workItem, updatingUser);
            return workItem;
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
            workItemService.delete(id, deletingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public WorkItem setStatusWithComment(@RequestHeader HttpHeaders headers, @PathVariable long id,
                                         @RequestParam long statusId, @RequestParam(required = false) String commentToAdd) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            WorkItem workItem = workItemService.getById(id);
            Status status = statusService.getById(statusId);
            Comment comment = addComment(commentToAdd, updatingUser);

            workItemService.setStatus(workItem, updatingUser, status, comment);
            return workItem;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public Comment addComment(String commentToAdd, User user) {
        Comment comment = new Comment();
        comment.setReviewer(user);
        comment.setComment(commentToAdd);
        commentService.create(comment);
        return comment;
    }

    @GetMapping("/filter")
    public List<WorkItem> filter(@RequestHeader HttpHeaders headers,
                                 @RequestParam(required = false) Optional<String> title,
                                 @RequestParam(required = false) Optional<String> status,
                                 @RequestParam(required = false) Optional<String> sortBy) {

        try {
            authenticationHelper.tryGetUser(headers);
            return workItemService.filter(title, status, sortBy);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
