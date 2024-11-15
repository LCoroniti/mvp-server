package com.tus.traunreut.webserver.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {
    /**
     * Get the current german time.
     */
    public static LocalDateTime nowGerman() {
        return LocalDateTime.now(ZoneId.of("Europe/Berlin"));
    }

    public static long nowGermanMillis() {
        return LocalDateTime.now().atZone(ZoneId.of("Europe/Berlin")).toInstant().toEpochMilli();
    }
}
