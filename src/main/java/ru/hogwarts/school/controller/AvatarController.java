package ru.hogwarts.school.controller;


import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.List;


@RestController
@RequestMapping(path = "/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping
    public List<Avatar> getAllPage(@RequestParam("numberPage") Integer numberPage, @RequestParam("sizePage")
    Integer sizePage) {
        return avatarService.getAllPage(numberPage, sizePage);
    }

}
