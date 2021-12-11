package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceIsAlreadyExistsException;
import com.tagall.tipsnbills.exceptions.ResourceIsNotValidException;
import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.exceptions.TokenRefreshException;
import com.tagall.tipsnbills.module.responses.ErrorMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageDto resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("There are some troubles with finding resource: {}", message);
        return message;
    }

    @ExceptionHandler(ResourceIsNotValidException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessageDto resourceIsNotValid(ResourceIsNotValidException ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.NOT_ACCEPTABLE.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("This resource is not valid: {}", message);
        return message;
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageDto refreshTokenException(TokenRefreshException ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("Refresh token time out: {}", message);
        return message;
    }

    @ExceptionHandler(ResourceIsAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageDto resourceIsAlreadyExistsException(ResourceIsAlreadyExistsException ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("This resource is already exists: {}", message);
        return message;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageDto accessDenied(Exception ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("Your token is expired or you need to authorize: {}", message);
        return message;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageDto globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessageDto message = new ErrorMessageDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        LOGGER.warn("Exception type: {}", ex.getClass());
        LOGGER.warn("Server sent error: {}", message);
        return message;
    }

}
