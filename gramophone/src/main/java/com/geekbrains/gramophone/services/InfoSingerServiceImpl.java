package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.InfoSinger;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.repositories.InfoSingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoSingerServiceImpl implements InfoSingerService{

    private InfoSingerRepository infoSingerRepository;
    private UserService userService;

    @Autowired
    public void setInfoSingerRepository(InfoSingerRepository infoSingerRepository) {
        this.infoSingerRepository = infoSingerRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void saveUserAsSinger(User user, String firstName, String lastName, String phone) {
        InfoSinger infoSinger = new InfoSinger();
        infoSinger.setFirstName(firstName);
        infoSinger.setLastName(lastName);
        infoSinger.setPhone(phone);
        infoSingerRepository.save(infoSinger);
        user.setInfoSinger(infoSinger);
        userService.save(user);
    }
}
