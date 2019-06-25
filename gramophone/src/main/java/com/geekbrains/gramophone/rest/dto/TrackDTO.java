package com.geekbrains.gramophone.rest.dto;

import lombok.Data;

@Data
public class TrackDTO {
    private Long id;

    public TrackDTO() {
    }

    public TrackDTO(Long id) {
        this.id = id;
    }
}
