# maven plugin

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
