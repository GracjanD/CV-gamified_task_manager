package com.gracjan_d.taskmanager.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(int id){
        super("Not found task with id: " + id);
    }
}
