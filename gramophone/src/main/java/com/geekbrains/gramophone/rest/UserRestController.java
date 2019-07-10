package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.SystemUser;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.services.InfoSingerService;
import com.geekbrains.gramophone.services.UploadService;
import com.geekbrains.gramophone.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/users")
@Api(tags = "Users")
public class UserRestController {

    private UserService userService;
    private InfoSingerService infoSingerService;
    private UploadService uploadService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setInfoSingerService(InfoSingerService infoSingerService) {
        this.infoSingerService = infoSingerService;
    }

    @Autowired
    public void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
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

    @GetMapping("/activate/{code}")
    public ResponseEntity<String> activate(@PathVariable("code") String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            return new ResponseEntity<>("активация прошла успешно", HttpStatus.OK);
        }
        return new ResponseEntity<>("активация не удалась, код активации был выслан на почту", HttpStatus.BAD_REQUEST);
    }

    //подписаться
    @PutMapping("/{user_id}/subscribe")
    public void subscribe(
            @PathVariable("user_id") Long userId,
            @RequestParam(value = "current_user_name") String currentUserName
    ) {
        User currentUser = userService.findByUsername(currentUserName);
        userService.subscribeOnUser(currentUser, userId);
    }

    //отписаться
    @PutMapping("/{user_id}/unsubscribe")
    public void unsubscribe(
            @PathVariable("user_id") Long userId,
            @RequestParam(value = "current_user_name") String currentUserName
    ) {

        User currentUser = userService.findByUsername(currentUserName);
        userService.unsubscribeOnUser(currentUser, userId);
    }

    // показать список подписок
    @GetMapping("/{user_id}/subscriptions")
    public Iterable<User> subscriptionsList(
            @PathVariable("user_id") Long userId
    ) {
        User user = userService.findById(userId);
        return user.getSubscriptions();
    }

    // показать список подписчиков
    @GetMapping("/{user_id}/subscribers")
    public Iterable<User> subscribersList(
            @PathVariable("user_id") Long userId
    ) {
        User user = userService.findById(userId);
        return user.getSubscribers();
    }

    //зарегистрироваться исполнителем
    @PutMapping("/confirm/singer")
    public ResponseEntity<String> confirmInfoSinger(
            @RequestParam(value = "current_user_name") String currentUserName,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("phone") String phone
    ) {
        try{
            User currentUser = userService.findByUsername(currentUserName);
            infoSingerService.saveUserAsSinger(currentUser, firstName, lastName, phone);
        }catch (Exception ex){
            return new ResponseEntity<>("Что-то пошло не так", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Вы успешно зарегистрировались в качестве исполнителя", HttpStatus.OK);
    }

    //загрузить аватар
    @PostMapping("/download/avatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "current_user_name") String currentUserName
    ) {
        User currentUser = userService.findByUsername(currentUserName);

        if (!file.isEmpty()) {
            if (uploadService.upload(currentUser.getUsername(), file, "images/")) {
                userService.changeAvatar(currentUser, file.getOriginalFilename());
            } else {
                return new ResponseEntity<>("Что-то пошло не так", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Файл для загрузки отсутствует", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Аватар загрузился", HttpStatus.OK);
    }

}
