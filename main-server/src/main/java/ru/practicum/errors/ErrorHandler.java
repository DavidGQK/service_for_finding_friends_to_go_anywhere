package ru.practicum.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.errors.exceptions.BadRequestException;
import ru.practicum.errors.exceptions.NotFoundException;
import ru.practicum.errors.exceptions.UnavailableStatisticsException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@RestControllerAdvice
public class ErrorHandler {
    private ResponseEntity<ApiError> makeResponse(Exception e, HttpStatus httpStatus, String message) {
        ApiError error = new ApiError(e, httpStatus, message);
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> unavailableStatisticsExceptionHandler(
            final UnavailableStatisticsException e) {
        return makeResponse(e, HttpStatus.INTERNAL_SERVER_ERROR,
                "The statistics service does not work");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> constraintViolationExceptionHandler(final ConstraintViolationException e) {
        return makeResponse(e, HttpStatus.BAD_REQUEST,
                "The request is incorrectly worded");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException e) {
        return makeResponse(e, HttpStatus.BAD_REQUEST,
                "The request is incorrectly worded");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> illegalArgumentExceptionHandler(final IllegalArgumentException e) {
        return makeResponse(e, HttpStatus.BAD_REQUEST,
                "Incorrect parameters in the operation");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> notFoundExceptionHandler(final NotFoundException e) {
        return makeResponse(e, HttpStatus.NOT_FOUND,
                "The requested object was not found in the database");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> sqlExceptionHandler(final SQLException e) {
        return makeResponse(e, HttpStatus.FORBIDDEN,
                "Database read/write error");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> badRequestExceptionHandler(final BadRequestException e) {
        return makeResponse(e, HttpStatus.FORBIDDEN,
                "Inadmissible action with an object");
    }
}