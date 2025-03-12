# gen-mybatis

自动生成 MyBatis 映射文件和接口的工具

[English](./README_EN.md)

## 项目简介

gen-mybatis 是一个用于自动生成 MyBatis 映射文件和接口的工具，它可以根据数据库表结构或实体类自动生成对应的 Mapper 接口和 XML 映射文件，减少手动编写重复代码的工作量。

## 功能特点

- 通过注解自动生成 MyBatis Mapper 接口和 XML 映射文件
- 支持 Lombok、JPA 和 Swagger 注解的生成
- 支持自定义表名和列名映射
- 提供完整的 CRUD 操作方法
- 支持批量插入和更新操作

## 使用方法

### 配置 Maven 插件

在您的项目 pom.xml 中添加以下配置：

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

### 创建实体类

使用 `@AutoGenMapper` 和 `@AutoGenColumn` 注解来标记您的实体类：

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

### 构建项目

执行 Maven 构建命令：

```
mvn clean compile
```

构建完成后，会在指定目录下生成 Mapper 接口和 XML 映射文件。

## 生成的文件示例

### Mapper 接口

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
    // 查询所有User记录
    List<User> selectAll();

    // 条件查询
    List<User> selectByFilter(User filters);

    // 根据ID查询
    User selectById(@Param("id") Long id);

    // 其他CRUD方法...
}
```

### XML 映射文件

生成的 XML 映射文件包含完整的 CRUD 操作，包括：

- 基本的结果映射
- 列名列表
- 查询、插入、更新、删除操作
- 批量操作

## 许可证

MIT License
