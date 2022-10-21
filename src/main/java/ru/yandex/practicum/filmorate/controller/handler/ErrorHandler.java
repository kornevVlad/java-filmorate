package ru.yandex.practicum.filmorate.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ServerError;
import ru.yandex.practicum.filmorate.exception.ValidationException;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handleIncorrectException(final ValidationException e) {
        return new ValidationException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException handleNotFoundException(final NotFoundException e) {
        return new NotFoundException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerError handleServerError(final ServerError e){
        return new ServerError(e.getMessage());
    }
}
