package com.geekbrains.gramophone.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "genres")
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

}
