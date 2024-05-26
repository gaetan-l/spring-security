package com.training.demo.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(final String email) {
        super(String.format("User with email=%s already exists", email));
        log.debug("UserAlreadyExistsException");
    }
}
