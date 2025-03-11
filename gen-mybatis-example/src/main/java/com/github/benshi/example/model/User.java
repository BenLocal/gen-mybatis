package com.github.benshi.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Optional;
import com.github.benshi.AutoGenMapper;
import com.github.benshi.AutoGenColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@AutoGenMapper(table = "user")
public class User {

    /**
     * 主键
     */
    @AutoGenColumn(name = "id", type = "BIGINT", nullable = false, pk = true)
    private Long id;

    /**
     * 名称
     */
    @AutoGenColumn(name = "name", type = "VARCHAR", nullable = false)
    private String name;

    /**
     * 年龄
     * 123
     */
    @AutoGenColumn(name = "age", type = "INT", nullable = false)
    private Integer age;

    public Optional<Long> getIdOptional() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

    public Optional<Integer> getAgeOptional() {
        return Optional.ofNullable(age);
    }

}