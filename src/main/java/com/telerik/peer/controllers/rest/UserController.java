package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.ReviewRequest;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.services.contracts.VerificationTokenService;
import com.telerik.peer.services.contracts.ReviewRequestService;
import com.telerik.peer.services.contracts.UserService;
import com.telerik.peer.utils.FileUploadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;
    private final ReviewRequestService reviewRequestService;
    private final VerificationTokenService verificationTokenService;



    @Autowired
    public UserController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper, ReviewRequestService reviewRequestService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
        this.reviewRequestService = reviewRequestService;
        this.verificationTokenService = verificationTokenService;
    }

    @GetMapping("{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return userService.getAll();
    }


    @PostMapping
    public User create(@Valid @RequestBody RegisterDto registerDto) {
        try {
            User user = userMapper.createUserFromRegisterDto(registerDto);
            userService.create(user);

            return user;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/photo")
    public User uploadPhoto(@RequestHeader HttpHeaders headers, @PathVariable long id,
                            @RequestParam(value = "file", required = false) MultipartFile multipartFile
    ) throws IOException {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            User user = userService.getById(id);
            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                user.setPhotoName(fileName);
                String uploadDir = "src/main/resources/user-photos/" + user.getId();
                FileUploadHelper.saveFile(uploadDir, fileName, multipartFile);
            }
            userService.update(user, loggedUser);
            return user;
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable long id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto, id);
            User updatingUser = authenticationHelper.tryGetUser(headers);
            userService.update(user, updatingUser);
            return user;
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
            User updatingUser = authenticationHelper.tryGetUser(headers);
            userService.delete(id, updatingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/requests/creator")
    public List<ReviewRequest> getReviewRequestByCreator(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        authenticationHelper.tryGetUser(headers);
        return reviewRequestService.getReviewRequestByCreator(id);
    }

    @GetMapping("/{id}/requests/reviewer")
    public List<ReviewRequest> getReviewRequestByReviewer(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        authenticationHelper.tryGetUser(headers);
        return reviewRequestService.getReviewRequestByReviewer(id);
    }

    @GetMapping("/search")
    public List<User> search(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) Optional<String> username,
                             @RequestParam(required = false) Optional<String> email,
                             @RequestParam(required = false) Optional<String> number
    ) {
        authenticationHelper.tryGetUser(headers);
        return userService.search(username, email, number);
    }


}
