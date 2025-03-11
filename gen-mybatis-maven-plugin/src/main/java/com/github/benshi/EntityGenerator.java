package com.github.benshi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.logging.Log;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class EntityGenerator {
    private final Log log;
    private final String outputDirectory;
    private final String packageName;
    private final Configuration freemarkerConfig;
    private final boolean generateLombok;
    private final boolean generateJPA;
    private final boolean generateSwagger;
    private final Set<String> importedTypes = new HashSet<>();

    public EntityGenerator(Log log, String outputDirectory, String packageName, boolean generateLombok,
            boolean generateJPA, boolean generateSwagger) {
        this.log = log;
        this.outputDirectory = outputDirectory;
        this.packageName = packageName;
        this.generateLombok = generateLombok;
        this.generateJPA = generateJPA;
        this.generateSwagger = generateSwagger;

        // Initialize FreeMarker configuration
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfig.setLogTemplateExceptions(false);
        freemarkerConfig.setWrapUncheckedExceptions(true);
        freemarkerConfig.setFallbackOnNullLoopVariable(false);
    }

    public void generateEntities(List<TableMetadata> tables) throws IOException, TemplateException {
        // Create output directory if it doesn't exist
        File packageDir = new File(outputDirectory + File.separator + packageName.replace('.', File.separatorChar));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        // Create entity template
        Template entityTemplate = freemarkerConfig.getTemplate("entity.ftl", "UTF-8");

        // Generate entity for each table
        for (TableMetadata table : tables) {
            generateEntity(table, entityTemplate, packageDir);
            log.info("Generated entity class: " + table.getClassName());
        }
    }

    private void generateEntity(TableMetadata table, Template template, File packageDir)
            throws IOException, TemplateException {
        // Prepare the data model for the template
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("tableName", table.getTableName());
        dataModel.put("className", table.getClassName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("generateLombok", generateLombok);
        dataModel.put("generateJPA", generateJPA);
        dataModel.put("generateSwagger", generateSwagger);

        // Process columns and collect imports
        importedTypes.clear();
        List<Map<String, Object>> columns = new ArrayList<>();

        for (ColumnMetadata column : table.getColumns()) {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("columnName", column.getColumnName());
            columnMap.put("fieldName", column.getFieldName());
            columnMap.put("javaType", column.getJavaType());
            columnMap.put("dataType", column.getDataType());

            String javaTypeSimple = TypeMapper.getSimpleJavaType(column.getJavaType());
            columnMap.put("javaTypeSimple", javaTypeSimple);

            // Add import if needed
            if (!column.getJavaType().startsWith("java.lang.") &&
                    !column.getJavaType().equals("byte[]")) {
                importedTypes.add(column.getJavaType());
            }

            columnMap.put("columnComment", column.getColumnComment());
            columnMap.put("isPrimaryKey", column.isPrimaryKey());
            columnMap.put("isNullable", column.isNullable());
            columnMap.put("isAutoIncrement", column.isAutoIncrement());

            columns.add(columnMap);
        }

        dataModel.put("columns", columns);
        dataModel.put("imports", importedTypes);

        // Generate the entity class file
        File entityFile = new File(packageDir, table.getClassName() + ".java");
        try (Writer writer = new FileWriter(entityFile)) {
            template.process(dataModel, writer);
        }
    }
}
