package com.telerik.peer.mappers;


import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private UserRepository userService;


    @Autowired
    public UserMapper(UserRepository userService) {
        this.userService = userService;

    }

    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());;
        userDto.setNumber(user.getNumber());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, int id) {
        User user = userService.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setNumber(userDto.getNumber());
        user.setPassword(user.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhotoName(user.getPhotoName());
    }


    public User createUserFromRegisterDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setNumber(registerDto.getNumber());

        return user;
    }


}


