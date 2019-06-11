package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.UploadTrackService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class UserAccountController {

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

    @GetMapping("/users-list")
    public String showAllUserPage(Model model){
        model.addAttribute("users", userService.findAll());
        return "all-users";
    }


    @RequestMapping("/my-page/{id}")
    public String showUserPage(
            Principal principal,
            @PathVariable("id") Long id,
            Model model
    ) {
        User user = userService.findById(id).get();
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser", currentUser.getId().equals(user.getId()));

        if(user.getSinger()){
            return "singer-page";
        }
        return "user-page";
    }

    //подписаться
    @GetMapping("subscribe/{userId}")
    public String subscribe(
            @PathVariable("userId") Long userId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        userService.subscribeOnUser(currentUser, userId);

        return "redirect:/my-page/" + userId;
    }

    //отписаться
    @GetMapping("unsubscribe/{userId}")
    public String unsubscribe(
            @PathVariable("userId") Long userId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        userService.unsubscribeOnUser(currentUser, userId);

        return "redirect:/my-page/" + userId;
    }

    // показать список подписчиков и подписок
    @GetMapping("{type}/{userId}/userList")
    public String userList(
            @PathVariable("type") String type,
            @PathVariable("userId") Long userId,
            Principal principal,
            Model model
    ) {
        User user = userService.findById(userId).get();
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }


    @PostMapping("/download-avatar")
    public String uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Principal principal,
            Model model
    ) {
        User currentUser = userService.findByUsername(principal.getName());

        if (!file.isEmpty()) {
            if (uploadTrackService.upload(principal.getName(), file, "images/")) {
                currentUser.setAvatar("images/" + currentUser.getUsername() + "/" + file.getOriginalFilename());
                userService.save(currentUser);
            } else {
                model.addAttribute("imgDownloadError", "Произошел сбой во время загрузки фото");
            }
        }

        return "redirect:/my-page/" + currentUser.getId();
    }
}
