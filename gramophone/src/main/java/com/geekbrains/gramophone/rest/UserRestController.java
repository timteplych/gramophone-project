package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/users")
@Api(tags = "Users")
public class UserRestController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }


    @GetMapping("/login")
    public User login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        return userService.findByEmail(email, password);
    }

    @PostMapping("/register")
    public User registration(@RequestBody SystemUser newUser) {
        return userService.save(newUser);
    }
}
