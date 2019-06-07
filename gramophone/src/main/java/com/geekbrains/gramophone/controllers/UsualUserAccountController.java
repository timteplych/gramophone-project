package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.UploadTrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class UsualUserAccountController {

    private UserService userService;
    private UploadTrackService uploadTrackService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUploadTrackService(UploadTrackService uploadTrackService) {
        this.uploadTrackService = uploadTrackService;
    }

    @RequestMapping("/user-page/{id}")
    public String showUserPage(
            @PathVariable("id") Long id,
            Model model
    ) {
        User user = userService.findById(id).get();
        model.addAttribute("user", user);
        return "user-page";
    }

    @PostMapping("/download-avatar")
    public String uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            Model model
    ) {
        User user = userService.findByUsername(principal.getName());

        if (!file.isEmpty()) {
            if (uploadTrackService.upload(principal.getName(), file, "images/")) {
                user.setAvatar("images/" + user.getUsername() + "/" + file.getOriginalFilename());
                userService.save(user);
            } else {
                model.addAttribute("imgDownloadError", "Произошел сбой во время загрузки фото");
            }
        }
        return "redirect:/user-page/" + user.getId();
    }
}
