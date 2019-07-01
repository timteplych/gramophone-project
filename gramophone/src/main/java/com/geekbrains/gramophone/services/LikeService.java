package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Like;
import com.geekbrains.gramophone.entities.LikeType;
import com.geekbrains.gramophone.entities.User;

import java.util.List;

public interface LikeService {
    void changeLike(Long userId, Long objectId, LikeType type);
    void changeDislike(Long userId, Long objectId, LikeType type);
    List<User> getLikes(Long objectId, LikeType type);
    List<User> getDislikes(Long objectId, LikeType type);
}
