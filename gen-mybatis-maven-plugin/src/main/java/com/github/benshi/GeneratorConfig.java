package com.github.benshi;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

public class GeneratorConfig {
    @Parameter(required = true)
    private String url;

    @Parameter(required = true)
    private String username;

    @Parameter(required = true)
    private String password;

    @Parameter
    private String schema;

    @Parameter(required = true)
    private String packageName;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/java")
    private String outputDirectory;

    @Parameter
    private List<String> includeTables = new ArrayList<>();

    @Parameter
    private List<String> excludeTables = new ArrayList<>();

    @Parameter(defaultValue = "true")
    private boolean generateLombok;

    @Parameter(defaultValue = "false")
    private boolean generateJPA;

    @Parameter(defaultValue = "false")
    private boolean generateSwagger;

    @Parameter(defaultValue = "false")
    private boolean generateOptional;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getIncludeTables() {
        return includeTables;
    }

    public List<String> getExcludeTables() {
        return excludeTables;
    }

    public boolean isGenerateLombok() {
        return generateLombok;
    }

    public boolean isGenerateJPA() {
        return generateJPA;
    }

    public boolean isGenerateSwagger() {
        return generateSwagger;
    }

    public boolean isGenerateOptional() {
        return generateOptional;
    }
}
