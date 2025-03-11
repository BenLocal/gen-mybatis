The `README_EN.md` file should be an English version of the Chinese README. Based on the context from your codebase, it appears that you want me to create this English version. Here's what the content of `README_EN.md` should be:

# gen-mybatis

A tool for automatically generating MyBatis mapping files and interfaces

[Chinese Version](README.md)

## Project Introduction

gen-mybatis is a tool for automatically generating MyBatis mapping files and interfaces. It can automatically generate corresponding Mapper interfaces and XML mapping files based on database table structures or entity classes, reducing the workload of manually writing repetitive code.

## Features

- Automatically generate MyBatis Mapper interfaces and XML mapping files through annotations
- Support for generating Lombok, JPA, and Swagger annotations
- Support for custom table and column name mapping
- Provide complete CRUD operation methods
- Support for batch insert and update operations

## Usage

### Configure Maven Plugin

Add the following configuration to your project's pom.xml:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>8</source>
        <target>8</target>
        <fork>true</fork>
        <compilerArgs>
            <arg>-J-Dautogen.mapper.output.dir=${project.basedir}/src/main/java</arg>
            <arg>-J-Dautogen.mapper.xml.output.dir=${project.basedir}/src/main/resources/mapper</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

### Create Entity Class

Use the `@AutoGenMapper` and `@AutoGenColumn` annotations to mark your entity class:

```java
package com.example.model;

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
    @AutoGenColumn(name = "id", type = "BIGINT", nullable = false, pk = true)
    private Long id;

    @AutoGenColumn(name = "name", type = "VARCHAR", nullable = false)
    private String name;

    @AutoGenColumn(name = "age", type = "INT")
    private Integer age;
}
```

### Build the Project

Execute the Maven build command:

```
mvn clean compile
```

After the build is complete, Mapper interfaces and XML mapping files will be generated in the specified directory.

## Generated File Examples

### Mapper Interface

```java
package com.example.model.mapper;

import com.example.model.User;
import java.lang.Long;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * MyBatis Mapper for UserMapper
 * Generated automatically by AutoGenMapperProcessor
 */
@Mapper
public interface UserMapper {
    // Query all User records
    List<User> selectAll();

    // Query by filter
    List<User> selectByFilter(User filters);

    // Query by ID
    User selectById(@Param("id") Long id);

    // Other CRUD methods...
}
```

### XML Mapping File

The generated XML mapping file contains complete CRUD operations, including:

- Basic result mapping
- Column name list
- Query, insert, update, and delete operations
- Batch operations

## License

MIT License
