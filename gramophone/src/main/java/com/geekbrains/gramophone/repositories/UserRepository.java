package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUsername(String username);
}
