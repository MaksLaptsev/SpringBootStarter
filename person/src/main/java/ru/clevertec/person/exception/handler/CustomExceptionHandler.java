package ru.clevertec.person.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.person.exception.PersonException;
import ru.clevertec.person.exception.dto.ExceptionResponse;
import ru.clevertec.starter.exception.BlackListLogin;
import ru.clevertec.starter.exception.ConnectionException;
import ru.clevertec.starter.exception.InvalidLoginValue;
import ru.clevertec.starter.exception.InvalidMethodParams;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<?> handlerConnectionException(ConnectionException exception){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(BlackListLogin.class)
    public ResponseEntity<?> handlerConnectionException(BlackListLogin exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidLoginValue.class)
    public ResponseEntity<?> handlerConnectionException(InvalidLoginValue exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(InvalidMethodParams.class)
    public ResponseEntity<?> handlerConnectionException(InvalidMethodParams exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(PersonException.class)
    public ResponseEntity<?> handlerConnectionException(PersonException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(exception.getMessage()));
    }
}
