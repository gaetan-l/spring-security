package com.training.demo.controller;

import com.training.demo.exception.EntityException;
import com.training.demo.exception.MalformedJsonException;
import com.training.demo.exception.TutorialNotFoundException;
import com.training.demo.exception.UserAlreadyExistsException;
import com.training.demo.util.ApiUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TutorialNotFoundException.class)
    public ModelAndView handleTutorialNotFoundException(TutorialNotFoundException e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("exception-handler");
        mv.setStatus(HttpStatus.NOT_FOUND);
        mv.addObject("message", e.getMessage());
        return mv;
    }

    @ExceptionHandler(EntityException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityException e) {
        return e.getResponseEntity();
    }

    @ExceptionHandler(MalformedJsonException.class)
    public ResponseEntity<String> handleJsonProcessingException(MalformedJsonException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ApiUtil.putExceptionInResponseHeaders(httpHeaders, e);
        return new ResponseEntity<>(
                e.getResponseBody(),
                httpHeaders,
                HttpStatus.BAD_REQUEST
        );
    }

    // Si jamais on veut catcher l'exception d'authentification de authentication/login
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleEntityNotFoundException(AuthenticationException e) {
        return new ResponseEntity<>(
                "",
                new HttpHeaders(),
                HttpStatus.I_AM_A_TEAPOT
        );
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return "toto";
    }
}