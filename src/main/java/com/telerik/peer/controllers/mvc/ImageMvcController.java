package com.telerik.peer.controllers.mvc;

import com.telerik.peer.models.Image;
import com.telerik.peer.services.contracts.ImageService;
import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/image")
public class ImageMvcController {
    ImageService imageService;
    UserService userService;

    @Autowired
    public ImageMvcController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @PostMapping
    Long uploadImage(@RequestParam MultipartFile multipartFile) throws Exception {
        Image image = new Image();
        image.setName(multipartFile.getName());
        image.setImage(multipartFile.getBytes());
        return imageService.create(image);

    }
}
