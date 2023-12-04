package ru.clevertec.starter.exception;

public class BlackListLogin extends RuntimeException{
    public BlackListLogin(String message) {
        super(message);
    }
}
