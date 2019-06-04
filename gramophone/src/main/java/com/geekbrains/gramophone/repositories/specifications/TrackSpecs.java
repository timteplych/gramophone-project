package com.geekbrains.gramophone.repositories.specifications;

import com.geekbrains.gramophone.entities.Track;
import org.springframework.data.jpa.domain.Specification;

public class TrackSpecs {
    public static Specification<Track> titleContains(String word) {
        return (Specification<Track>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }

    public static Specification<Track> wordAuthorContains(String word) {
        return (Specification<Track>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("wordAuthor"), "%" + word + "%");
    }

    public static Specification<Track> musicAuthorContains(String word) {
        return (Specification<Track>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("musicAuthor"), "%" + word + "%");
    }

}
