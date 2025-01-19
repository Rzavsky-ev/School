package ru.hogwarts.school.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("object not found");
    }
}
