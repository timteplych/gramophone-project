package com.geekbrains.gramophone.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RestException {

    private String message;
    private HttpStatus status;
}
