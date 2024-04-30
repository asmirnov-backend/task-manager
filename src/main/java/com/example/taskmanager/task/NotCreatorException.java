package com.example.taskmanager.task;

public class NotCreatorException extends Exception {
    NotCreatorException() {
        super("Not a creator");
    }
}
