package com.example.taskmanager.user;


public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String existAttribute) {
        super(String.format("User with such $s already exist", existAttribute));
    }
}
