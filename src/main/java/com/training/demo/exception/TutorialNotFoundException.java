package com.training.demo.exception;

public class TutorialNotFoundException extends RuntimeException {
    public TutorialNotFoundException(Long id) {
        super(String.format("Tutorial with id=%s not found", id));
    }
}