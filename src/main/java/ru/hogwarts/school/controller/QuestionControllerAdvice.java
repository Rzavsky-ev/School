package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.exception.EntityNotFoundException;

@ControllerAdvice
public class QuestionControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> methodNotAllowedException(EntityNotFoundException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
