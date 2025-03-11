package com.github.benshi;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@SupportedAnnotationTypes("com.github.benshi.AutoGenMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoGenMapperProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Messager messager;
    private Filer filer;
    private Configuration freemarkerConfig;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        freemarkerConfig = initFreeMarker();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    String packageName = elementUtils.getPackageOf(element).getQualifiedName()
                            .toString();
                    String className = element.getSimpleName().toString();
                    // get AutoGenMapper annotation
                    AutoGenMapper mapperAnnotation = element.getAnnotation(AutoGenMapper.class);

                    // get columns info
                    List<GenElementColumnInfo> columns = processColumns((TypeElement) element);
                    GenElementInfo info = new GenElementInfo((TypeElement) element,
                            mapperAnnotation, packageName,
                            columns);
                    messager.printMessage(Kind.NOTE,
                            "Processing class: " + className + " with table name: "
                                    + info.tableName());
                    try {
                        generateMapperInterface(info);
                        generateMapperXmlFile(info);
                        processingEnv.getMessager().printMessage(Kind.NOTE,
                                "generate mapper interface success");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TemplateException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return true;
    }

    private Configuration initFreeMarker() {
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfig.setLogTemplateExceptions(false);
        freemarkerConfig.setWrapUncheckedExceptions(true);
        freemarkerConfig.setFallbackOnNullLoopVariable(false);

        return freemarkerConfig;
    }

    private List<GenElementColumnInfo> processColumns(TypeElement element) {
        List<GenElementColumnInfo> columns = new ArrayList<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.FIELD) {
                continue;
            }
            VariableElement variableElement = (VariableElement) enclosedElement;
            AutoGenColumn columnAnnotation = variableElement.getAnnotation(AutoGenColumn.class);
            String fieldName = variableElement.getSimpleName().toString();

            GenElementColumnInfo columnInfo = new GenElementColumnInfo();
            columnInfo.setFieldName(fieldName);
            columnInfo.setColumnName(
                    VariableElementColumnUtils.getColumnName(variableElement, columnAnnotation,
                            fieldName));
            columnInfo.setPrimaryKey(
                    VariableElementColumnUtils.isPrimaryKey(variableElement, columnAnnotation));

            TypeMirror fieldType = variableElement.asType();
            columnInfo.setFieldType(fieldType);
            columnInfo.setJdbcType(
                    VariableElementColumnUtils.getJdbcType(variableElement, columnAnnotation,
                            fieldType.toString()));
            columnInfo.setNullable(
                    VariableElementColumnUtils.isNullable(variableElement, columnAnnotation));

            columns.add(columnInfo);
        }
        return columns;
    }

    private String getMapperPackageName(GenElementInfo info) {
        return String.format("%s.%s", info.getPackageName(), "mapper");
    }

    private String getMapperClassName(GenElementInfo info) {
        return String.format("%sMapper", info.getSimpleClassName());
    }

    private void generateMapperInterface(GenElementInfo info) throws IOException {
        String mapperPackageName = getMapperPackageName(info);
        String mapperSimpleClassName = getMapperClassName(info);

        processingEnv.getMessager().printMessage(Kind.NOTE,
                String.format("generate mapper interface %s.%s", mapperPackageName,
                        mapperSimpleClassName));

        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(mapperSimpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.apache.ibatis.annotations", "Mapper"))
                .addJavadoc("MyBatis Mapper for $L\n", mapperSimpleClassName)
                .addJavadoc("Generated automatically by AutoGenMapperProcessor\n");

        TypeName entityType = ClassName.get(info.getPackageName(), info.getSimpleClassName());
        ParameterizedTypeName listType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                entityType);

        // selectAll 方法
        MethodSpec selectAllMethod = MethodSpec.methodBuilder("selectAll")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(listType)
                .addJavadoc("查询所有$L记录\n", info.getSimpleClassName())
                .addJavadoc("@return 所有记录列表\n")
                .build();
        interfaceBuilder.addMethod(selectAllMethod);

        // selectByFilter 方法
        MethodSpec selectByFilterMethod = MethodSpec.methodBuilder("selectByFilter")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "filters")
                .returns(listType)
                .addJavadoc("查询$L记录\n", info.getSimpleClassName())
                .addJavadoc("@return 所有条件查询列表\n")
                .build();
        interfaceBuilder.addMethod(selectByFilterMethod);

        // selectOneByFilter
        MethodSpec selectOneByFilterMethod = MethodSpec.methodBuilder("selectOneByFilter")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "filters")
                .returns(entityType)
                .addJavadoc("查询$L记录\n", info.getSimpleClassName())
                .addJavadoc("@return 所有条件查询信息\n")
                .build();
        interfaceBuilder.addMethod(selectOneByFilterMethod);

        List<GenElementColumnInfo> ids = info.getPrimaryKeyColumns();
        if (ids != null && ids.size() > 0) {
            // selectById 方法
            MethodSpec.Builder selectByIdMethodBuilder = MethodSpec.methodBuilder("selectById")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ClassName.get(
                            info.getPackageName(),
                            info.getSimpleClassName()));

            for (GenElementColumnInfo id : ids) {
                AnnotationSpec paramAnnotation = AnnotationSpec
                        .builder(ClassName.get("org.apache.ibatis.annotations", "Param"))
                        .addMember("value", "$S", id.getFieldName())
                        .build();
                ParameterSpec parameterSpec = ParameterSpec.builder(id
                        .getJavaTypeName(), id.getFieldName())
                        .addAnnotation(paramAnnotation)
                        .build();
                selectByIdMethodBuilder.addParameter(parameterSpec);
            }

            // javadoc
            selectByIdMethodBuilder.addJavadoc("根据ID查询$L\n", info.getSimpleClassName());
            for (GenElementColumnInfo id : ids) {
                selectByIdMethodBuilder.addJavadoc("@param $L 主键ID\n", id.getFieldName());
            }
            selectByIdMethodBuilder.addJavadoc("@return 对应的记录，如果不存在则返回null\n");
            MethodSpec selectByIdMethod = selectByIdMethodBuilder
                    .build();
            interfaceBuilder.addMethod(selectByIdMethod);

            // deleteById 方法
            MethodSpec.Builder deleteByIdMethodBuilder = MethodSpec.methodBuilder("deleteById")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(TypeName.INT);

            for (GenElementColumnInfo id : ids) {
                AnnotationSpec paramAnnotation = AnnotationSpec
                        .builder(ClassName.get("org.apache.ibatis.annotations", "Param"))
                        .addMember("value", "$S", id.getFieldName())
                        .build();
                ParameterSpec parameterSpec = ParameterSpec.builder(id
                        .getJavaTypeName(), id.getFieldName())
                        .addAnnotation(paramAnnotation)
                        .build();
                deleteByIdMethodBuilder.addParameter(parameterSpec);
            }

            // javadoc
            deleteByIdMethodBuilder.addJavadoc("根据ID删除$L\n", info.getSimpleClassName());
            for (GenElementColumnInfo id : ids) {
                deleteByIdMethodBuilder.addJavadoc("@param $L 主键ID\n", id.getFieldName());
            }
            deleteByIdMethodBuilder.addJavadoc("@return 受影响的行数\n");
            MethodSpec deleteByIdMethod = deleteByIdMethodBuilder
                    .build();
            interfaceBuilder.addMethod(deleteByIdMethod);
        }

        // update 方法
        MethodSpec updateMethod = MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.INT)
                .addParameter(entityType, "entity")
                .addJavadoc("更新$L记录\n", info.getSimpleClassName())
                .addJavadoc("@param entity 要更新的实体对象\n")
                .addJavadoc("@return 受影响的行数\n")
                .build();

        interfaceBuilder.addMethod(updateMethod);

        // insert 方法
        MethodSpec insertMethod = MethodSpec.methodBuilder("insert")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.INT)
                .addParameter(ClassName.get(info.getPackageName(), info.getSimpleClassName()), "entity")
                .addJavadoc("插入新的$L记录\n", info.getSimpleClassName())
                .addJavadoc("@param entity 要插入的实体对象\n")
                .addJavadoc("@return 受影响的行数\n")
                .build();

        interfaceBuilder.addMethod(insertMethod);

        // count 方法
        MethodSpec countMethod = MethodSpec.methodBuilder("count")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(entityType, "filters")
                .returns(TypeName.LONG)
                .addJavadoc("统计$L记录数\n", info.getSimpleClassName())
                .addJavadoc("@param filters 查询条件\n")
                .addJavadoc("@return 记录数\n")
                .build();

        interfaceBuilder.addMethod(countMethod);

        // batch insert 方法
        MethodSpec batchInsertMethod = MethodSpec.methodBuilder("batchInsert")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.INT)
                .addParameter(listType, "entities")
                .addJavadoc("批量插入$L记录\n", info.getSimpleClassName())
                .addJavadoc("@param entities 要插入的实体对象\n")
                .addJavadoc("@return 受影响的行数\n")
                .build();

        interfaceBuilder.addMethod(batchInsertMethod);

        // batch update 方法
        MethodSpec batchUpdateMethod = MethodSpec.methodBuilder("batchUpdate")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.INT)
                .addParameter(listType, "entities")
                .addJavadoc("批量更新$L记录\n", info.getSimpleClassName())
                .addJavadoc("@param entities 要更新的实体对象\n")
                .addJavadoc("@return 受影响的行数\n")
                .build();

        interfaceBuilder.addMethod(batchUpdateMethod);

        JavaFile javaFile = JavaFile.builder(mapperPackageName, interfaceBuilder.build())
                .indent("    ")
                .build();

        javaFile.writeTo(filer);
    }

    private void generateMapperXmlFile(GenElementInfo info) throws IOException, TemplateException {
        String mapperPackageName = getMapperPackageName(info);
        String mapperSimpleClassName = getMapperClassName(info);

        String entityNamespace = ClassName.get(
                info.getPackageName(),
                info.getSimpleClassName()).toString();
        Properties properties = new Properties();
        properties.put("mapperNamespace", mapperPackageName + "." + mapperSimpleClassName);
        properties.put("entityNamespace", entityNamespace);

        // create baseResultMap
        List<Properties> baseResultMapProperties = new ArrayList<>();
        StringBuilder agileSB = new StringBuilder();
        for (GenElementColumnInfo columnInfo : info.columns()) {
            Properties cp = new Properties();
            cp.put("column", columnInfo.getColumnName());
            cp.put("jdbcType", columnInfo.getJdbcType());
            cp.put("property", columnInfo.getFieldName());
            cp.put("pk", columnInfo.isPrimaryKey());

            baseResultMapProperties.add(cp);
            agileSB.append(",").append("`").append(columnInfo.getFieldName()).append("`");
        }

        properties.put("baseResultMap", baseResultMapProperties);
        properties.put("agile", agileSB.length() == 0 ? "" : agileSB.substring(1));
        properties.put("table", info.tableName());

        String relativePath = "mappers/" + mapperSimpleClassName + ".xml";
        Template template = freemarkerConfig.getTemplate("mapper.ftl");
        FileObject fileObject = filer.createResource(
                StandardLocation.CLASS_OUTPUT,
                "",
                relativePath);
        try (Writer writer = fileObject.openWriter()) {
            template.process(properties, writer);
        }
    }
}
