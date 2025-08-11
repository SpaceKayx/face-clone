package com.core.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

    public static String generatedStringFormat(String concatWith, Object... data) {
        return String.join(concatWith,
                java.util.Arrays.stream(data)
                        .map(String::valueOf)
                        .toArray(String[]::new));
    }

}
