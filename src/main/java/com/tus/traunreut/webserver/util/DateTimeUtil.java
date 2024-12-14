package com.tus.traunreut.webserver.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
}
