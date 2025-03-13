package com.github.benshi.mybatis;

import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.TypeName;

public class GenElementColumnInfo {
    private String fieldName;
    private String columnName;
    private boolean isPrimaryKey;
    private boolean nullable;
    private String jdbcType;

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    private TypeMirror fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public TypeMirror getFieldType() {
        return fieldType;
    }

    public void setFieldType(TypeMirror fieldType) {
        this.fieldType = fieldType;
    }

    public TypeName getJavaTypeName() {
        return TypeName.get(fieldType);
    }

    public String getJavaTypeString() {
        return fieldType.toString();
    }
}
