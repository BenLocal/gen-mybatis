package com.github.benshi.mybatis;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetadataReader {
    private final String url;
    private final String username;
    private final String password;
    private final String schema;
    private final List<String> includeTables;
    private final List<String> excludeTables;

    public DatabaseMetadataReader(String url, String username, String password, String schema,
            List<String> includeTables, List<String> excludeTables) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.schema = schema;
        this.includeTables = includeTables;
        this.excludeTables = excludeTables;
    }

    public List<TableMetadata> readTables() throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            // catalog和schemaPattern参数需要根据数据库类型正确设置
            // MySQL通常使用数据库名作为catalog，schemaPattern为null
            // Oracle通常使用null作为catalog，用户名大写作为schemaPattern
            // SQL Server通常使用数据库名作为catalog，dbo作为schemaPattern
            // PostgreSQL通常使用null作为catalog，用户名大写作为schemaPattern

            // 从URL提取数据库名
            String catalog = getDatabaseName(url);
            // 对于MySQL，通常为null
            String schemaPattern = schema;
            // 获取所有表
            String tableNamePattern = "%";
            // 只获取表，不包括视图等
            String[] types = { "TABLE" };
            try (ResultSet tablesResultSet = metaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {

                while (tablesResultSet.next()) {
                    String tableName = tablesResultSet.getString("TABLE_NAME");

                    // Skip tables based on include/exclude lists
                    if (!shouldProcessTable(tableName)) {
                        continue;
                    }

                    String tableComment = tablesResultSet.getString("REMARKS");
                    if (tableComment == null) {
                        tableComment = "";
                    }

                    String className = TypeMapper.convertSnakeCaseToPascalCase(tableName);
                    TableMetadata tableMetadata = new TableMetadata(tableName, className, tableComment);

                    readColumns(connection, tableMetadata, catalog);
                    tables.add(tableMetadata);
                }
            }
        }

        return tables;
    }

    private boolean shouldProcessTable(String tableName) {
        if (includeTables != null && !includeTables.isEmpty()) {
            for (String pattern : includeTables) {
                if (matchesWildcard(tableName, pattern)) {
                    // Check exclusions before including
                    if (excludeTables != null && !excludeTables.isEmpty()) {
                        for (String excludePattern : excludeTables) {
                            if (matchesWildcard(tableName, excludePattern)) {
                                // Table matches an exclude pattern
                                return false;
                            }
                        }
                    }
                    // Table matches an include pattern and not excluded
                    return true;
                }
            }
            // Table doesn't match any include pattern
            return false;
        }

        if (excludeTables != null && !excludeTables.isEmpty()) {
            for (String pattern : excludeTables) {
                if (matchesWildcard(tableName, pattern)) {
                    // Table matches an exclude pattern
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the table name matches a wildcard pattern.
     * Supports * for any sequence of characters and ? for a single character.
     * Matching is case insensitive.
     * 
     * @param tableName The table name to check
     * @param pattern   The pattern to match against
     * @return true if the table name matches the pattern
     */
    private boolean matchesWildcard(String tableName, String pattern) {
        // Convert wildcard pattern to regex pattern
        // Replace * with .* (any sequence of characters)
        // Replace ? with . (any single character)
        String regexPattern = pattern
                .replace(".", "\\.") // Escape dots first to avoid conflicts
                .replace("*", ".*")
                .replace("?", ".");

        // Match the table name against the regex pattern, case insensitive
        // (?i) enables case-insensitive matching in regex
        return tableName.matches("(?i)" + regexPattern);
    }

    private void readColumns(Connection connection, TableMetadata tableMetadata, String catalog) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String tableName = tableMetadata.getTableName();

        // Get primary keys
        List<String> primaryKeys = new ArrayList<>();
        try (ResultSet primaryKeysResultSet = metaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (primaryKeysResultSet.next()) {
                primaryKeys.add(primaryKeysResultSet.getString("COLUMN_NAME"));
            }
        }

        // Get columns
        try (ResultSet columnsResultSet = metaData.getColumns(catalog, schema, tableName, "%")) {
            while (columnsResultSet.next()) {
                String columnName = columnsResultSet.getString("COLUMN_NAME");
                String fieldName = TypeMapper.convertSnakeCaseToCamelCase(columnName);
                if (JavaKeywordsUtils.isJavaKeyword(fieldName) || JavaKeywordsUtils.startWithNumberKeyword(fieldName)) {
                    fieldName = String.format("_%s", fieldName);
                }

                int dataType = columnsResultSet.getInt("DATA_TYPE");
                String typeName = columnsResultSet.getString("TYPE_NAME");
                String javaType = TypeMapper.getJavaType(dataType);
                String columnComment = columnsResultSet.getString("REMARKS");
                if (columnComment == null) {
                    columnComment = "";
                }

                boolean isPrimaryKey = primaryKeys.contains(columnName);
                boolean isNullable = columnsResultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                int size = columnsResultSet.getInt("COLUMN_SIZE");
                int decimalDigits = columnsResultSet.getInt("DECIMAL_DIGITS");
                boolean isAutoIncrement = "YES".equals(columnsResultSet.getString("IS_AUTOINCREMENT"));

                ColumnMetadata columnMetadata = new ColumnMetadata(
                        columnName, fieldName, typeName, javaType, columnComment,
                        isPrimaryKey, isNullable, size, decimalDigits, isAutoIncrement);

                tableMetadata.addColumn(columnMetadata);
            }
        }
    }

    private String getDatabaseName(String url) {
        // 例如从jdbc:mysql://localhost:3306/mydb中提取mydb
        int lastSlashIndex = url.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            String dbNameWithParams = url.substring(lastSlashIndex + 1);
            int questionMarkIndex = dbNameWithParams.indexOf("?");
            if (questionMarkIndex != -1) {
                return dbNameWithParams.substring(0, questionMarkIndex);
            }
            return dbNameWithParams;
        }
        return null;
    }
}
