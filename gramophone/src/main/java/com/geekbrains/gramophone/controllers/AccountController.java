package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user-page/{id}")
    public String showUserPage(
            @PathVariable("id") Long id,
            Model model
    ) {
        User user = userService.findById(id).get();
        model.addAttribute("user", user);

        if(user.getSinger()){
            return "singer-page";
        }
        return "user-page";
    }
}
