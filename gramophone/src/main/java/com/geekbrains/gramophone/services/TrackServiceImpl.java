package com.geekbrains.gramophone.services;

import com.geekbrains.gramophone.entities.Track;
import com.geekbrains.gramophone.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;

    @Autowired
    public void setTrackRepository(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public List<Track> findAll() {
        return (List<Track>) trackRepository.findAll();
    }

    @Override
    public boolean save(Track track) {
        trackRepository.save(track);
        return true;
    }

    public Page<Track> getProductsWithPagingAndFiltering(int pageNumber, int pageSize, Specification<Track> productSpecification) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        return trackRepository.findAll(productSpecification, PageRequest.of(pageNumber, pageSize, sort));
    }
}
