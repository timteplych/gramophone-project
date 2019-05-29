package com.geekbrains.gramophone.utils;

import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.jpa.domain.Specification;

public class TrackSpecification {

    public static Specification<Track> titleContains(String word) {
        return (Specification<Track>) (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }
}
