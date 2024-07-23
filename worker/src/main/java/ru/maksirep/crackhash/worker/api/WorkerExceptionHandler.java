package ru.maksirep.crackhash.worker.api;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.maksirep.crackhash.worker.core.exceptions.WorkerException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class WorkerExceptionHandler {

    static final Logger logger = LoggerFactory.getLogger(WorkerExceptionHandler.class);

    private static final String INTERNAL_EXCEPTION = "Неизвестная ошибка";
    private static final String INCORRECT_ARGUMENT_TYPE = "Некорректный вводимый тип";

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorMessage handleRuntimeException(RuntimeException ex) {
        logger.error(ex.getMessage());
        return new ErrorMessage(INTERNAL_EXCEPTION);
    }

    @ExceptionHandler(value = {WorkerException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleManagerException(WorkerException ex) {
        logger.error(ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage());
        return new ErrorMessage(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error(ex.getMessage());
        return new ErrorMessage(ex.getConstraintViolations().stream().toList().get(0).getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error(ex.getMessage());
        return new ErrorMessage(INCORRECT_ARGUMENT_TYPE);
    }
}
