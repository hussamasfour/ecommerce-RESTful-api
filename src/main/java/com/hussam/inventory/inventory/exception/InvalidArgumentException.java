package com.hussam.inventory.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidArgumentException extends RuntimeException{

    public InvalidArgumentException(String message){
        super(message);
    }
}