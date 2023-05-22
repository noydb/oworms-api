package com.oworms.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static final ZoneId TIME_ZONE = ZoneId.of("Africa/Johannesburg");

    private Utils() {
    }

    public static boolean isBlank(String arg) {
        return arg == null || arg.trim().equals("");
    }

    public static boolean areEqual(String arg, String argTheSecond) {
        if (arg == null || argTheSecond == null) {
            return arg == null && argTheSecond == null;
        }

        final String argTrimmedAndLowered = arg.trim().toLowerCase(Locale.ROOT);
        final String argThe2ndTrimmed = argTheSecond.trim().toLowerCase(Locale.ROOT);

        return argTrimmedAndLowered.toLowerCase(Locale.ROOT).contains(argThe2ndTrimmed.toLowerCase(Locale.ROOT));
    }

    /**
     * Some string fields on a Word could have commas in them.
     * This wraps the values that do in "" so the CSV does not
     * split inadvertently
     *
     * @param input string will be checked for "," value(s)
     * @return the argument wrapped in double quotes if commas
     * are present or the argument if
     * no commas are present or null if the argument is null
     */
    public static String wrapTextIfCommaPresent(String input) {
        if (Utils.isBlank(input)) {
            return null;
        }

        if (input.contains(",")) {
            return "\"" + input + "\"";
        }

        return input;
    }

    public static String format(LocalDateTime dateTime, String format) {
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(format);

        return dateTime.format(dtFormat);
    }
}
