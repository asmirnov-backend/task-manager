package com.example.taskmanager.user;

public class CurrentPasswordIsIncorrectException extends Exception{

    public CurrentPasswordIsIncorrectException() {
        super("Current password is incorrect");
    }
}
