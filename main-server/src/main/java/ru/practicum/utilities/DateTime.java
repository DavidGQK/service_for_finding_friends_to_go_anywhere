package ru.practicum.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static LocalDateTime stringToDateTime(String value) {
        if (value == null) {
            return null;
        }
        return LocalDateTime.parse(value, formatter);
    }

    public static String dateTimeToString(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return formatter.format(value);
    }
}