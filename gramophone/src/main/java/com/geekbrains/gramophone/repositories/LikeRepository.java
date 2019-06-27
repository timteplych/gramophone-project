package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Like;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {

}
