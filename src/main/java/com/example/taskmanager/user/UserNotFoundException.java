package com.example.taskmanager.user;

public class UserNotFoundException extends Error{
    UserNotFoundException() {
        super("User not found");
    }
}
