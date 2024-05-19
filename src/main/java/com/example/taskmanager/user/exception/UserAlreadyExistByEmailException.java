package com.example.taskmanager.user.exception;


public class UserAlreadyExistByEmailException extends UserAlreadyExistException {
    public UserAlreadyExistByEmailException() {
        super("email");
    }
}
