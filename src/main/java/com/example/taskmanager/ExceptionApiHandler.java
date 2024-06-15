package com.example.taskmanager;

import com.example.taskmanager.auth.exception.IncorrectCredentialsException;
import com.example.taskmanager.auth.exception.InvalidRefreshTokenException;
import com.example.taskmanager.common.dto.ErrorResponseDto;
import com.example.taskmanager.common.exception.NotCreatorException;
import com.example.taskmanager.user.exception.CurrentPasswordIsIncorrectException;
import com.example.taskmanager.user.exception.UserAlreadyExistByEmailException;
import com.example.taskmanager.user.exception.UserAlreadyExistByUsernameException;
import com.example.taskmanager.user.exception.UserAlreadyExistException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto notFoundException(NotFoundException exception) {
        return new ErrorResponseDto((exception.getMessage() != null && !exception.getMessage().isEmpty()) ? exception.getMessage() : "Entity not found");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto invalidRefreshTokenException(InvalidRefreshTokenException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto incorrectCredentialsException(IncorrectCredentialsException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @ExceptionHandler({UserAlreadyExistByUsernameException.class, UserAlreadyExistByEmailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto userAlreadyExistByUsernameException(UserAlreadyExistException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @ExceptionHandler(NotCreatorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto notCreatorException(NotCreatorException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @ExceptionHandler(CurrentPasswordIsIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto currentPasswordIsIncorrectException(CurrentPasswordIsIncorrectException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

}
