package com.github.benshi;

@AutoGenMapper
public class User {
    @AutoGenColumn(name = "uid", pk = true, type = "bigint")
    private long id;
    @AutoGenColumn(name = "name", type = "VARCHAR(255)")
    private String name;
    @AutoGenColumn(name = "age", type = "int")
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
