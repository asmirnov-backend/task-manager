package com.example.taskmanager.user.exception;


public class UserAlreadyExistByUsernameException extends UserAlreadyExistException {
    public UserAlreadyExistByUsernameException() {
        super("username");
    }
}
