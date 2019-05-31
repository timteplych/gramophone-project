package com.geekbrains.gramophone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// just test comment

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

}
