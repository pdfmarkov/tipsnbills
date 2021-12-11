package com.tagall.tipsnbills.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ResourceIsNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceIsNotValidException(String msg) {
        super(msg);
    }

}
