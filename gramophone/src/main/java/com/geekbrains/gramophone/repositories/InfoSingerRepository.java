package com.geekbrains.gramophone.repositories;

import com.geekbrains.gramophone.entities.InfoSinger;
import com.geekbrains.gramophone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoSingerRepository extends CrudRepository<InfoSinger, Long> {

}
