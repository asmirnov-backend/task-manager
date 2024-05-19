package com.example.taskmanager.user.exception;


public abstract class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String existAttribute) {
        super("User with such " + existAttribute + " already exist");
    }
}
