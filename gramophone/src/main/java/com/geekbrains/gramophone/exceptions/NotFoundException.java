package com.geekbrains.gramophone.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String what, Long id) {
        super(what + " not found: " + id);
    }
}
