package com.github.benshi.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.github.benshi.AutoGenMapper;
import com.github.benshi.AutoGenColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AutoGenMapper(table = "user")
public class User {

    @AutoGenColumn(name = "id", type = "BIGINT" , nullable = false, pk = true)
    private Long id;

    @AutoGenColumn(name = "name", type = "VARCHAR" , nullable = false)
    private String name;

    @AutoGenColumn(name = "age", type = "INT" , nullable = false)
    private Integer age;

}