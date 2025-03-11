package com.github.benshi;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {
    private String tableName;
    private String className;
    private String tableComment;
    private List<ColumnMetadata> columns = new ArrayList<>();

    public TableMetadata(String tableName, String className, String tableComment) {
        this.tableName = tableName;
        this.className = className;
        this.tableComment = tableComment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }

    public void addColumn(ColumnMetadata column) {
        this.columns.add(column);
    }

    @Override
    public String toString() {
        return "TableMetadata{" +
                "tableName='" + tableName + '\'' +
                ", className='" + className + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", columns=" + columns +
                '}';
    }
}
