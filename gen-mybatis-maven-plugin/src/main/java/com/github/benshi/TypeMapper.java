package com.github.benshi;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class TypeMapper {
    private static final Map<Integer, String> SQL_TYPE_TO_JAVA_TYPE = new HashMap<>();

    static {
        // Numeric types
        SQL_TYPE_TO_JAVA_TYPE.put(Types.BIT, Boolean.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.TINYINT, Byte.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.SMALLINT, Short.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.INTEGER, Integer.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.BIGINT, Long.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.FLOAT, Float.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.REAL, Float.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.DOUBLE, Double.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.NUMERIC, BigDecimal.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.DECIMAL, BigDecimal.class.getName());

        // String types
        SQL_TYPE_TO_JAVA_TYPE.put(Types.CHAR, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.VARCHAR, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.LONGVARCHAR, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.NCHAR, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.NVARCHAR, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.LONGNVARCHAR, String.class.getName());

        // Date and time types
        SQL_TYPE_TO_JAVA_TYPE.put(Types.DATE, LocalDate.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.TIME, LocalTime.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.TIMESTAMP, LocalDateTime.class.getName());

        // Binary types
        SQL_TYPE_TO_JAVA_TYPE.put(Types.BINARY, byte[].class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.VARBINARY, byte[].class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.LONGVARBINARY, byte[].class.getName());

        // Other types
        SQL_TYPE_TO_JAVA_TYPE.put(Types.BLOB, byte[].class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.CLOB, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.NCLOB, String.class.getName());
        SQL_TYPE_TO_JAVA_TYPE.put(Types.BOOLEAN, Boolean.class.getName());
    }

    public static String getJavaType(int sqlType) {
        String javaType = SQL_TYPE_TO_JAVA_TYPE.get(sqlType);
        return javaType != null ? javaType : Object.class.getName();
    }

    public static String getSimpleJavaType(String fullJavaType) {
        int lastDot = fullJavaType.lastIndexOf('.');
        return lastDot > 0 ? fullJavaType.substring(lastDot + 1) : fullJavaType;
    }

    public static String convertSnakeCaseToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;

        for (int i = 0; i < snakeCase.length(); i++) {
            char currentChar = snakeCase.charAt(i);
            if (currentChar == '_') {
                nextIsUpper = true;
            } else {
                if (nextIsUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextIsUpper = false;
                } else {
                    if (i == 0) {
                        result.append(Character.toLowerCase(currentChar));
                    } else {
                        result.append(currentChar);
                    }
                }
            }
        }

        return result.toString();
    }

    public static String convertSnakeCaseToPascalCase(String snakeCase) {
        String camelCase = convertSnakeCaseToCamelCase(snakeCase);
        if (camelCase.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
    }
}
