package com.edith.developmentassistant.utils;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String defaultIfNullOrEmpty(String value, String defaultValue) {
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }

    public static <T> List<T> defaultIfNullOrEmpty(List<T> value, List<T> defaultValue) {
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }
}
