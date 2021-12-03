package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.WorkItemMapper;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.models.dto.WorkItemDto;
import com.telerik.peer.services.contracts.CommentService;
import com.telerik.peer.services.contracts.TeamService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/workitems")
public class WorkItemController {
    AuthenticationHelper authenticationHelper;
    WorkItemService workItemService;
    TeamService teamService;
    CommentService commentService;
    UserService userService;
    WorkItemMapper workItemMapper;

    @Autowired
    public WorkItemController(AuthenticationHelper authenticationHelper, WorkItemService workItemService, TeamService teamService, CommentService commentService, UserService userService, WorkItemMapper workItemMapper) {
        this.authenticationHelper = authenticationHelper;
        this.workItemService = workItemService;
        this.teamService = teamService;
        this.commentService = commentService;
        this.userService = userService;
        this.workItemMapper = workItemMapper;
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
    public WorkItem update(@RequestHeader HttpHeaders headers,@PathVariable long id,@Valid @RequestBody WorkItemDto workItemDto){
        try{
            User updatingUser=authenticationHelper.tryGetUser(headers);
            WorkItem workItem=workItemMapper.fromDto(workItemDto,id);
            workItemService.update(workItem,updatingUser);
            return workItem;
        }catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
@DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers,@PathVariable long id){
        try{
            User deletingUser=authenticationHelper.tryGetUser(headers);
            workItemService.delete(id,deletingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
}

}
