package com.github.benshi.mybatis.example.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Optional;
import com.github.benshi.mybatis.AutoGenMapper;
import com.github.benshi.mybatis.AutoGenColumn;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
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

    public void setIdOptional(Optional<Long> idOptional) {
        if (idOptional == null) {
            this.id = null;
            return;
        }

        this.id = idOptional.orElse(null);
    }
    
    public Optional<String> getNameOptional() {
        return Optional.ofNullable(name);
    }

    public void setNameOptional(Optional<String> nameOptional) {
        if (nameOptional == null) {
            this.name = null;
            return;
        }

        this.name = nameOptional.orElse(null);
    }
    
    public Optional<Integer> getAgeOptional() {
        return Optional.ofNullable(age);
    }

    public void setAgeOptional(Optional<Integer> ageOptional) {
        if (ageOptional == null) {
            this.age = null;
            return;
        }

        this.age = ageOptional.orElse(null);
    }
    
}