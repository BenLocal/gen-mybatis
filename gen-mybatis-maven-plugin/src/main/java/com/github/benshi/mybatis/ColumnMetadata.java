package com.github.benshi.mybatis;

public class ColumnMetadata {
    private String columnName;
    private String fieldName;
    private String dataType;
    private String javaType;
    private String columnComment;
    private boolean isPrimaryKey;
    private boolean isNullable;
    private int size;
    private int decimalDigits;
    private boolean isAutoIncrement;

    public ColumnMetadata(String columnName, String fieldName, String dataType, String javaType,
            String columnComment, boolean isPrimaryKey, boolean isNullable,
            int size, int decimalDigits, boolean isAutoIncrement) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.javaType = javaType;
        this.columnComment = columnComment;
        this.isPrimaryKey = isPrimaryKey;
        this.isNullable = isNullable;
        this.size = size;
        this.decimalDigits = decimalDigits;
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    @Override
    public String toString() {
        return "ColumnMetadata{" +
                "columnName='" + columnName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", javaType='" + javaType + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isNullable=" + isNullable +
                ", size=" + size +
                ", decimalDigits=" + decimalDigits +
                ", isAutoIncrement=" + isAutoIncrement +
                '}';
    }
}
