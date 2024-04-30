package com.example.taskmanager;

import com.example.taskmanager.auth.IncorrectCredentialsException;
import com.example.taskmanager.auth.InvalidRefreshTokenException;
import com.example.taskmanager.task.NotCreatorException;
import com.example.taskmanager.user.UserAlreadyExistException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectCredentialsException(IncorrectCredentialsException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userAlreadyExistsException(IncorrectCredentialsException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NotCreatorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse notCreatorException(NotCreatorException exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
