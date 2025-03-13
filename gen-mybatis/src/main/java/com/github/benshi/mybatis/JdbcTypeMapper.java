package com.github.benshi.mybatis;

import java.util.HashMap;
import java.util.Map;

public class JdbcTypeMapper {
    private static final Map<String, String> SQL_TO_JDBC_TYPE_MAP = new HashMap<>();

    static {
        // 字符串类型
        SQL_TO_JDBC_TYPE_MAP.put("VARCHAR", "VARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("VARCHAR2", "VARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("CHAR", "CHAR");
        SQL_TO_JDBC_TYPE_MAP.put("TEXT", "LONGVARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("CLOB", "CLOB");
        SQL_TO_JDBC_TYPE_MAP.put("LONGTEXT", "LONGVARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("MEDIUMTEXT", "LONGVARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("NVARCHAR", "NVARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("NCHAR", "NCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("NTEXT", "LONGNVARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("NCLOB", "NCLOB");

        // 数值类型
        SQL_TO_JDBC_TYPE_MAP.put("INT", "INTEGER");
        SQL_TO_JDBC_TYPE_MAP.put("INTEGER", "INTEGER");
        SQL_TO_JDBC_TYPE_MAP.put("TINYINT", "TINYINT");
        SQL_TO_JDBC_TYPE_MAP.put("SMALLINT", "SMALLINT");
        SQL_TO_JDBC_TYPE_MAP.put("MEDIUMINT", "INTEGER");
        SQL_TO_JDBC_TYPE_MAP.put("BIGINT", "BIGINT");

        // 浮点类型
        SQL_TO_JDBC_TYPE_MAP.put("FLOAT", "FLOAT");
        SQL_TO_JDBC_TYPE_MAP.put("DOUBLE", "DOUBLE");
        SQL_TO_JDBC_TYPE_MAP.put("DECIMAL", "DECIMAL");
        SQL_TO_JDBC_TYPE_MAP.put("NUMERIC", "NUMERIC");
        SQL_TO_JDBC_TYPE_MAP.put("REAL", "REAL");

        // 日期时间类型
        SQL_TO_JDBC_TYPE_MAP.put("DATE", "DATE");
        SQL_TO_JDBC_TYPE_MAP.put("TIME", "TIME");
        SQL_TO_JDBC_TYPE_MAP.put("DATETIME", "TIMESTAMP");
        SQL_TO_JDBC_TYPE_MAP.put("TIMESTAMP", "TIMESTAMP");
        SQL_TO_JDBC_TYPE_MAP.put("YEAR", "DATE");

        // 布尔类型
        SQL_TO_JDBC_TYPE_MAP.put("BOOLEAN", "BOOLEAN");
        SQL_TO_JDBC_TYPE_MAP.put("BIT", "BIT");

        // 二进制类型
        SQL_TO_JDBC_TYPE_MAP.put("BINARY", "BINARY");
        SQL_TO_JDBC_TYPE_MAP.put("VARBINARY", "VARBINARY");
        SQL_TO_JDBC_TYPE_MAP.put("BLOB", "BLOB");
        SQL_TO_JDBC_TYPE_MAP.put("LONGBLOB", "BLOB");
        SQL_TO_JDBC_TYPE_MAP.put("MEDIUMBLOB", "BLOB");

        // 其他类型
        SQL_TO_JDBC_TYPE_MAP.put("JSON", "VARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("ENUM", "VARCHAR");
        SQL_TO_JDBC_TYPE_MAP.put("SET", "VARCHAR");
    }

    /**
     * 将SQL数据类型转换为MyBatis的JdbcType
     * 
     * @param sqlType SQL数据类型，如VARCHAR(255)、INT等
     * @return MyBatis的JdbcType，如VARCHAR、INTEGER等
     */
    public static String toJdbcType(String sqlType) {
        if (sqlType == null || sqlType.isEmpty()) {
            return "VARCHAR";
        }

        // 提取基本类型（去掉长度和精度信息）
        String baseType = sqlType.replaceAll("\\(.*\\)", "").trim().toUpperCase();

        // 查找映射
        String jdbcType = SQL_TO_JDBC_TYPE_MAP.get(baseType);

        // 如果找不到映射，默认使用VARCHAR
        return jdbcType != null ? jdbcType : "VARCHAR";
    }

    /**
     * 从Java类型推断JDBC类型
     * 
     * @param javaType Java类型的全限定名
     * @return 对应的JDBC类型
     */
    public static String fromJavaType(String javaType) {
        if (javaType == null || javaType.isEmpty()) {
            return "VARCHAR";
        }

        switch (javaType) {
            case "java.lang.String":
                return "VARCHAR";
            case "java.lang.Integer":
            case "int":
                return "INTEGER";
            case "java.lang.Long":
            case "long":
                return "BIGINT";
            case "java.lang.Float":
            case "float":
                return "FLOAT";
            case "java.lang.Double":
            case "double":
                return "DOUBLE";
            case "java.math.BigDecimal":
                return "DECIMAL";
            case "java.lang.Boolean":
            case "boolean":
                return "BOOLEAN";
            case "java.util.Date":
                return "TIMESTAMP";
            case "java.sql.Date":
                return "DATE";
            case "java.sql.Time":
                return "TIME";
            case "java.sql.Timestamp":
                return "TIMESTAMP";
            case "byte[]":
                return "BLOB";
            default:
                return "VARCHAR";
        }
    }
}
