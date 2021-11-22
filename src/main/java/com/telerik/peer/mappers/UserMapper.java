package com.telerik.peer.mappers;


import com.telerik.peer.models.Image;
import com.telerik.peer.models.User;
import com.telerik.peer.models.dto.RegisterDto;
import com.telerik.peer.models.dto.UserDto;
import com.telerik.peer.repositories.contracts.ImageRepository;
import com.telerik.peer.repositories.contracts.UserRepository;
import com.telerik.peer.services.contracts.ImageService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private UserService userService;
    private ImageService imageService;

    @Autowired
    public UserMapper(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setNumber(user.getNumber());
        userDto.setEmail(user.getEmail());
        userDto.setImageId(user.getImage().getId());

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
        Image image = imageService.getById(userDto.getImageId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setNumber(user.getNumber());
        user.setImage(image);
    }

    public Image createImageFromRegisterDto(RegisterDto registerDto) {
        Image image = new Image();
        image.setImage(registerDto.getImage());
        imageService.create(image);
        return image;
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


