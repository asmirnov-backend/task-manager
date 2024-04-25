package com.example.taskmanager.user;


public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String existAttribute) {
        super(String.format("User with such $s already exist", existAttribute));
    }
}
