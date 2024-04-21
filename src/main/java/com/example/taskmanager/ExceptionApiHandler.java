package com.example.taskmanager;

import com.example.taskmanager.auth.InvalidRefreshTokenException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(NotFoundException exception) {
        return new ErrorResponse((exception.getMessage() != null && !exception.getMessage().isEmpty()) ? exception.getMessage() : "Entity not found");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse invalidRefreshTokenException(InvalidRefreshTokenException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
