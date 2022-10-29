package com.example.restschool.models.group.exceptions;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String msg){
        super(msg);
    }
}
