package com.github.benshi.mybatis;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EntityGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter
    private GeneratorConfig config;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting database entity generation...");

        // Validate configuration
        if (config == null) {
            throw new MojoExecutionException("Configuration is missing. Please provide database connection details.");
        }

        // Create output directory if it doesn't exist
        File outputDir = new File(config.getOutputDirectory());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Add the output directory to the project's source root
        project.addCompileSourceRoot(config.getOutputDirectory());

        // Read database metadata
        getLog().info("Reading database metadata from " + config.getUrl());
        DatabaseMetadataReader metadataReader = new DatabaseMetadataReader(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getSchema(),
                config.getIncludeTables(),
                config.getExcludeTables());

        try {
            List<TableMetadata> tables = metadataReader.readTables();

            getLog().info("Found " + tables.size() + " tables to process");

            // Generate entity classes
            EntityGenerator entityGenerator = new EntityGenerator(
                    getLog(),
                    config.getOutputDirectory(),
                    config.getPackageName(),
                    config.isGenerateLombok(),
                    config.isGenerateJPA(),
                    config.isGenerateSwagger(),
                    config.isGenerateOptional(),
                    config.isGenerateJackson());

            entityGenerator.generateEntities(tables);

            getLog().info("Successfully generated " + tables.size() + " entity classes");
        } catch (Exception e) {
            throw new MojoExecutionException("Error generating entity classes", e);
        }
    }

}
