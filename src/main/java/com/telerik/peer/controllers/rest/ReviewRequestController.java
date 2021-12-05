package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.InvalidUserInputException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.ReviewRequestMapper;
import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.ReviewRequestDto;
import com.telerik.peer.services.contracts.ReviewRequestService;
import com.telerik.peer.services.contracts.WorkItemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/review-requests")
public class ReviewRequestController {

    private final ReviewRequestService reviewRequestService;
    private final WorkItemService workItemService;
    private final UserController userController;
    private final ReviewRequestMapper reviewRequestMapper;
    private final AuthenticationHelper authenticationHelper;

    public ReviewRequestController(ReviewRequestService reviewRequestService, WorkItemService workItemService,
                                   UserController userController, ReviewRequestMapper reviewRequestMapper,
                                   AuthenticationHelper authenticationHelper) {
        this.reviewRequestService = reviewRequestService;
        this.workItemService = workItemService;
        this.userController = userController;
        this.reviewRequestMapper = reviewRequestMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<ReviewRequest> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return reviewRequestService.getAll();
    }

    @GetMapping("{id}")
    public ReviewRequest getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return reviewRequestService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping

    public ReviewRequest create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            ReviewRequest reviewRequest = reviewRequestMapper.fromDto(reviewRequestDto);
            reviewRequestService.create(reviewRequest);
            return reviewRequest;
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ReviewRequest update(@RequestHeader HttpHeaders headers, @PathVariable long id, @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            ReviewRequest reviewRequest = reviewRequestMapper.fromDto(reviewRequestDto, id);
            reviewRequestService.update(reviewRequest, updatingUser);
            return reviewRequest;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User deletingUser = authenticationHelper.tryGetUser(headers);
            reviewRequestService.delete(id, deletingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
