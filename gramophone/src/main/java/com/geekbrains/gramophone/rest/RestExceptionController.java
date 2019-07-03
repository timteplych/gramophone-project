package com.geekbrains.gramophone.rest;

import com.geekbrains.gramophone.entities.RestException;
import com.geekbrains.gramophone.exceptions.NotFoundException;
import com.geekbrains.gramophone.exceptions.ThereIsNoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.geekbrains.gramophone.rest")
public class RestExceptionController {

    @ExceptionHandler(ThereIsNoSuchUserException.class)
    protected ResponseEntity<RestException> handleThereIsNoSuchUserException() {
        return new ResponseEntity<>(new RestException("There is no such user", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected RestException handleNotFoundExceptionException(NotFoundException ex) {
        return new RestException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
