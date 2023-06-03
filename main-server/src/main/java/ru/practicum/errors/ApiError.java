package ru.practicum.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ApiError {
    private static final String TIMESTAMP_FORMAT = "dd.MM.yyyy hh:mm:ss";
    private String id;
    private String timestamp;
    private String status;
    private String reason;
    private String message;
    private String[] errors;

    public ApiError(Exception e, HttpStatus httpStatus, String message) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = getTimestampInString();
        this.status = httpStatus.name();
        this.reason = e.getMessage();
        this.message = message;
        this.errors = getStackTrace(e);
    }

    private String getTimestampInString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
        return formatter.format(LocalDateTime.now());
    }

    private String[] getStackTrace(Exception e) {
        List<String> list = new ArrayList<>();
        for (StackTraceElement el : e.getStackTrace()) {
            list.add(el.toString());
        }
        return list.toArray(new String[list.size()]);
    }
}