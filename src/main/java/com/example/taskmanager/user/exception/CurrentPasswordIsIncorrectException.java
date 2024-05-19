package com.example.taskmanager.user.exception;

public class CurrentPasswordIsIncorrectException extends Exception {

    public CurrentPasswordIsIncorrectException() {
        super("Current password is incorrect");
    }
}
