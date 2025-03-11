# Database Entity Generator Maven Plugin

A Maven plugin that generates Java entity classes from database tables.

## Features

- Generates Java entity classes from database tables
- Supports multiple database types (MySQL, PostgreSQL, Oracle, SQL Server)
- Customizable entity generation with Lombok, JPA, and Swagger annotations
- Filters tables to include or exclude
- Converts database column names to Java field names (snake_case to camelCase)
- Adds appropriate Java types based on database column types
- Includes comments from database tables and columns

## Usage

Add the plugin to your Maven project:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.benshi</groupId>
            <artifactId>gen-mybatis-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <config>
                    <url>jdbc:mysql://localhost:3306/your_database</url>
                    <username>your_username</username>
                    <password>your_password</password>
                    <schema>your_schema</schema>
                    <packageName>com.example.model</packageName>
                    <outputDirectory>${project.basedir}/src/main/java</outputDirectory>
                    <includeTables>
                        <includeTable>user</includeTable>
                        <includeTable>product</includeTable>
                    </includeTables>
                    <excludeTables>
                        <excludeTable>flyway_schema_history</excludeTable>
                    </excludeTables>
                    <generateLombok>true</generateLombok>
                    <generateJPA>true</generateJPA>
                    <generateSwagger>true</generateSwagger>
                </config>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Cmd

```shell
mvn com.github.benshi:gen-mybatis-maven-plugin:1.0-SNAPSHOT:generate
```
