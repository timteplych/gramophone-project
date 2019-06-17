package com.geekbrains.gramophone.controllers;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/registrationForm")
    public String showMyLoginPage(Model model) {
        model.addAttribute("systemUser", new SystemUser());
        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("systemUser") SystemUser systemUser,
            BindingResult bindingResult,
            Model model
    ) {
        String username = systemUser.getUsername();
        if (bindingResult.hasErrors()) {
            return "registration-form";
        }
        User existing = userService.findByUsername(username);
        if (existing != null) {
            model.addAttribute("systemUser", systemUser);
            model.addAttribute("registrationError", "User with current username already exists");
            return "registration-form";
        }
        userService.save(systemUser);
        return "registration-confirmation";
    }

    // todo Сообщить пользователю, что на его почтовый ящик отправлено письмо для активации
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable("code") String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageActivate", "Активация пользователя прошла успешно");
        } else {
            model.addAttribute("messageActivate", "Код активации не найден! Код выслан на указанный почтовый ящик.");
        }

        return "activate";
    }
}
