package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.User;

public interface InfoSingerService {
    void saveUserAsSinger(User user, String firstName, String lastName, String phone);
}
