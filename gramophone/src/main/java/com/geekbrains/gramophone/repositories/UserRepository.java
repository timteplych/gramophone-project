package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUsername(String username);
    User findByEmail(String email);
    User findByActivationCode(String code);

}
