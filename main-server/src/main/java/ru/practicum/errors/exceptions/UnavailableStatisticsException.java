package ru.practicum.errors.exceptions;

public class UnavailableStatisticsException extends RuntimeException {
    public UnavailableStatisticsException(String message) {
        super(message);
    }
}