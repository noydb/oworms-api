package com.power.util;

import com.power.dto.WordDTO;

import java.util.Locale;

public class WordUtil {

    private WordUtil() {
    }

    public static boolean isBlank(String arg) {
        return arg == null || arg.trim().equals("");
    }

    public static boolean isEqual(String arg, String argTheSecond) {
        if (arg == null || argTheSecond == null) {
            return arg == null && argTheSecond == null;
        }

        return clean(arg).contains(clean(argTheSecond));
    }

    private static String clean(String arg) {
        if (arg == null) {
            return null;
        }

        return arg.trim().toLowerCase(Locale.ROOT);
    }

    public static void clean(WordDTO wordDTO) {
        String theWord = wordDTO.getTheWord();
        if (!isBlank(theWord)) {
            wordDTO.setTheWord(clean(theWord));
        }

        String definition = wordDTO.getDefinition();
        if (!isBlank(definition)) {
            wordDTO.setDefinition(clean(definition));
        }
    }
}
