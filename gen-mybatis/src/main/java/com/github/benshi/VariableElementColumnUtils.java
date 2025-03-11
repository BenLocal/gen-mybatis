package com.github.benshi;

import javax.lang.model.element.VariableElement;

public class VariableElementColumnUtils {
    private VariableElementColumnUtils() {

    }

    public static String getColumnName(VariableElement variableElement, AutoGenColumn columnAnnotation,
            String fieldName) {
        String defautlName = AutoGenUtils.convertCamelCaseToSnakeCase(fieldName);
        if (columnAnnotation == null) {
            // default column name is field name
            return defautlName;
        } else {
            return columnAnnotation.name().isEmpty()
                    ? defautlName
                    : columnAnnotation.name();
        }
    }

    public static boolean isPrimaryKey(VariableElement variableElement, AutoGenColumn columnAnnotation) {
        if (columnAnnotation == null) {
            return false;
        } else {
            return columnAnnotation.pk();
        }
    }

    public static String getJdbcType(VariableElement variableElement, AutoGenColumn columnAnnotation,
            String fieldTypeString) {
        if (columnAnnotation == null) {
            return JdbcTypeMapper.fromJavaType(fieldTypeString);
        } else {
            return columnAnnotation.type().isEmpty()
                    ? JdbcTypeMapper.fromJavaType(fieldTypeString)
                    : JdbcTypeMapper.toJdbcType(columnAnnotation.type());
        }
    }

    public static String getComment(VariableElement variableElement, AutoGenColumn columnAnnotation) {
        if (columnAnnotation == null) {
            return "";
        } else {
            return columnAnnotation.comment();
        }
    }

    public static boolean isNullable(VariableElement variableElement, AutoGenColumn columnAnnotation) {
        if (columnAnnotation == null) {
            return true;
        } else {
            return columnAnnotation.nullable();
        }
    }
}
