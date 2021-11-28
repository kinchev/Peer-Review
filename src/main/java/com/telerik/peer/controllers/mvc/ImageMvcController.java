package com.telerik.peer.controllers.mvc;

import com.telerik.peer.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageMvcController {

    UserService userService;

    @Autowired
    public ImageMvcController(UserService userService) {

        this.userService = userService;
    }


}
