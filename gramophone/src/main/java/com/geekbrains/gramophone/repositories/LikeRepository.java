package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Like;
import com.geekbrains.gramophone.entities.LikeType;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {
    Like findByUserAndLikeTypeAndTargetId(User user, LikeType likeType, Long targetId);
    List<Like> findByLikeTypeAndMarkAndTargetId(LikeType likeType, byte likeOrDislike, Long targetId);

}
