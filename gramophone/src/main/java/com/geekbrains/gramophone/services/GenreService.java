package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
    Genre findByTitle(String title);
    Genre findById(Long id);
}
