package com.github.benshi;

public class AutoGenUtils {
    private AutoGenUtils() {
    }

    public static String convertCamelCaseToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        // 处理第一个字符
        result.append(Character.toLowerCase(camelCase.charAt(0)));

        // 处理剩余字符
        for (int i = 1; i < camelCase.length(); i++) {
            char currentChar = camelCase.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append('_');
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }
}
