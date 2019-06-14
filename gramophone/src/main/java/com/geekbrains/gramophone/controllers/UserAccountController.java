package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.InfoSingerService;
import com.geekbrains.gramophone.services.UploadService;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
public class UserAccountController {

    private UserService userService;
    private UploadService uploadTrackService;
    private InfoSingerService infoSingerService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUploadTrackService(UploadService uploadTrackService) {
        this.uploadTrackService = uploadTrackService;
    }

    @Autowired
    public void setInfoSingerService(InfoSingerService infoSingerService) {
        this.infoSingerService = infoSingerService;
    }

    @GetMapping("/users/list")
    public String showAllUserPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "all-users";
    }


    @RequestMapping("/users/{user_id}")
    public String showUserPage(
            Principal principal,
            @PathVariable("user_id") Long userId,
            Model model
    ) {
        User user = userService.findById(userId).get();
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser", currentUser.getId().equals(user.getId()));

        return "user-page";
    }

    //подписаться
    @GetMapping("/users/{user_id}/subscribe")
    public String subscribe(
            @PathVariable("user_id") Long userId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        userService.subscribeOnUser(currentUser, userId);

        return "redirect:/users/" + userId;
    }

    //отписаться
    @GetMapping("/users/{user_id}/unsubscribe")
    public String unsubscribe(
            @PathVariable("user_id") Long userId,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());
        userService.unsubscribeOnUser(currentUser, userId);

        return "redirect:/users/" + userId;
    }

    // показать список подписок
    @GetMapping("/users/{user_id}/subscriptions")
    public String subscriptionsList(
            @PathVariable("user_id") Long userId,
            Principal principal,
            Model model
    ) {
        User user = userService.findById(userId).get();
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", user.getSubscriptions());
        model.addAttribute("subTitle", "Мои подписки");

        return "subscriptions";
    }

    // показать список подписчиков
    @GetMapping("/users/{user_id}/subscribers")
    public String subscribersList(
            @PathVariable("user_id") Long userId,
            Principal principal,
            Model model
    ) {
        User user = userService.findById(userId).get();
        User currentUser = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", user.getSubscribers());
        model.addAttribute("subTitle", "Мои подписчики");

        return "subscriptions";
    }

    @PostMapping("/confirm/singer")
    public String confirmInfoSinger(
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("phone") String phone,
            Principal principal
    ) {
        User currentUser = userService.findByUsername(principal.getName());

        //добавить валидацию данных
        infoSingerService.saveUserAsSinger(currentUser, firstName, lastName, phone);

        return "redirect:/users/" + currentUser.getId();
    }

    @PostMapping("/download/avatar")
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

        return "redirect:/users/" + currentUser.getId(); // сообщить, что файл пустой аватар не загружен
    }
}
