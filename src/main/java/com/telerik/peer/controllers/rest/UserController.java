package com.telerik.peer.controllers.rest;

import com.telerik.peer.exceptions.DuplicateEntityException;
import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.mappers.UserMapper;
import com.telerik.peer.models.Image;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.services.contracts.ImageService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    public UserController(UserService userService, ImageService imageService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.imageService = imageService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping("{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByField("id", user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }


    @PostMapping
    public User create(@Valid @RequestBody RegisterDto registerDto) {
        try {
            Image image = userMapper.createImageFromRegisterDto(registerDto);
            User user = userMapper.createUserFromRegisterDto(registerDto);
            user.setImage(image);
            userService.create(user);
            return user;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
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
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User updatingUser = authenticationHelper.tryGetUser(headers);
            userService.delete(id, updatingUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


}
