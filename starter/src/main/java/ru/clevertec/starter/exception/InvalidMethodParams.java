package ru.clevertec.starter.exception;

public class InvalidMethodParams extends RuntimeException{
    public InvalidMethodParams(String message) {
        super(message);
    }
}
