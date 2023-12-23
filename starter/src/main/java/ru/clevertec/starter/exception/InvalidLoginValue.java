package ru.clevertec.starter.exception;

public class InvalidLoginValue extends RuntimeException {
    public InvalidLoginValue(String message) {
        super(message);
    }
}
