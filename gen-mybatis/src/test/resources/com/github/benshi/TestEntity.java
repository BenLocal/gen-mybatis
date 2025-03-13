package com.github.benshi.mybatis;

@AutoGenMapper(table = "user")
public class TestEntity {
    @AutoGenColumn(name = "id", type = "BIGINT", nullable = false, pk = true)
    private Long id;

    @AutoGenColumn(name = "name", type = "VARCHAR", nullable = false)
    private String name;
}
