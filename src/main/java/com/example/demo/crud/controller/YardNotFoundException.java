package com.example.CRUD.controller;

public class YardNotFoundException extends Throwable {
    public YardNotFoundException(String message) {
        super(message);
    }
}