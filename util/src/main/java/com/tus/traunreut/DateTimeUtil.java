package com.tus.traunreut;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {
    /**
     * Get the current german time.
     */
    public static LocalDateTime nowGerman() {
        return LocalDateTime.now(ZoneId.of("Europe/Berlin"));
    }

    public static long nowGermanSecondsRounded() {
        long epochSeconds = LocalDateTime.now()
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant()
                .getEpochSecond();
        return epochSeconds - (epochSeconds % 15);
    }

    public static String formatDate(LocalDateTime dateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMAN);
        return dateTime.format(formatter);
    }
}
