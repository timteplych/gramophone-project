package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Like;
import com.geekbrains.gramophone.entities.LikeType;
import com.geekbrains.gramophone.entities.User;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.repositories.LikeRepository;
import com.geekbrains.gramophone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void changeLike(Long userId, Long objectId, LikeType type) {
        changeSomething(userId, objectId, type, Like.LIKE);
    }

    @Override
    public void changeDislike(Long userId, Long objectId, LikeType type) {
        changeSomething(userId, objectId, type, Like.DISLIKE);
    }

    private void changeSomething(Long userId, Long objectId, LikeType type, byte mark) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", userId));
        Like like = likeRepository.findByUserAndLikeTypeAndTargetId(user, type, objectId);
        if (like == null) {
            like = new Like(type, user, objectId, mark);
        } else if (like.getMark() == mark) {
            like.setMark(Like.EMPTY);
        } else {
            like.setMark(mark);
        }
        likeRepository.save(like);
    }

    @Override
    public List<User> getLikes(Long objectId, LikeType type) {
        return getSomething(objectId, type, Like.LIKE);
    }

    @Override
    public List<User> getDislikes(Long objectId, LikeType type) {
        return getSomething(objectId, type, Like.DISLIKE);
    }

    private List<User> getSomething(Long objectId, LikeType type, byte mark) {
        return likeRepository.findByLikeTypeAndMarkAndTargetId(type, mark, objectId)
                .stream().map((element) -> element.getUser()).collect(Collectors.toList());

    }
}
