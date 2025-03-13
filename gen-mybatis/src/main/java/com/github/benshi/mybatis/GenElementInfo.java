package com.github.benshi.mybatis;

import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;

/**
 * 
 * 
 * @date 2025年3月10日
 * @time 21:43:00
 * @description
 * 
 */
public class GenElementInfo {
    private final TypeElement element;
    private final String packageName;
    private final String className;
    private final String simpleClassName;
    private final AutoGenMapper autoGenMapper;
    private final List<GenElementColumnInfo> columns;

    public GenElementInfo(TypeElement element, AutoGenMapper autoGenMapper, String packageName,
            List<GenElementColumnInfo> columns) {
        this.element = element;
        this.packageName = packageName;
        this.autoGenMapper = autoGenMapper;
        this.columns = columns;

        this.className = this.element.getQualifiedName().toString();
        this.simpleClassName = this.element.getSimpleName().toString();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public String tableName() {
        String tableName = autoGenMapper.table().isEmpty()
                ? AutoGenUtils.convertCamelCaseToSnakeCase(simpleClassName)
                : autoGenMapper.table();

        return tableName;
    }

    public List<GenElementColumnInfo> columns() {
        return columns;
    }

    public List<GenElementColumnInfo> getPrimaryKeyColumns() {
        return columns.stream().filter(GenElementColumnInfo::isPrimaryKey)
                .collect(Collectors.toList());
    }

    public TypeElement getElement() {
        return element;
    }
}
