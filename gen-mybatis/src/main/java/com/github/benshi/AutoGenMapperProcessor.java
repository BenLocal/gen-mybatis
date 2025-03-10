package com.github.benshi;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@SupportedAnnotationTypes("com.github.benshi.AutoGenMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoGenMapperProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
                    GenInfo info = new GenInfo((TypeElement) element, packageName);
                    try {
                        generateMapperInterface(info);
                        processingEnv.getMessager().printMessage(Kind.NOTE, "generate mapper interface success");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return true;
    }

    private void generateMapperInterface(GenInfo info) throws IOException {
        String mapperPackageName = String.format("%s.%s", info.getPackageName(), "mapper");
        String mapperSimpleClassName = info.getSimpleClassName() + "Mapper";

        processingEnv.getMessager().printMessage(Kind.NOTE,
                String.format("generate mapper interface %s.%s", mapperPackageName, mapperSimpleClassName));

        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(mapperSimpleClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.apache.ibatis.annotations", "Mapper"))
                .addJavadoc("MyBatis Mapper for $L\n", mapperSimpleClassName)
                .addJavadoc("Generated automatically by AutoGenMapperProcessor\n");
        // 添加 selectAll 方法
        ParameterizedTypeName listType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                ClassName.get(info.getPackageName(), info.getSimpleClassName()));

        MethodSpec selectAllMethod = MethodSpec.methodBuilder("selectAll")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(listType)
                .addJavadoc("查询所有$L记录\n", info.getSimpleClassName())
                .addJavadoc("@return 所有记录列表\n")
                .build();

        interfaceBuilder.addMethod(selectAllMethod);

        // 如果有主键，添加按 ID 查询的方法
        // if (idField.isPresent()) {
        // FieldInfo id = idField.get();
        // TypeName idType = getTypeName(id.fieldType);

        // // selectById 方法
        // MethodSpec selectByIdMethod = MethodSpec.methodBuilder("selectById")
        // .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        // .returns(ClassName.get(entityInfo.packageName, entityInfo.simpleClassName))
        // .addParameter(idType, id.fieldName)
        // .addJavadoc("根据ID查询$L\n", entityInfo.simpleClassName)
        // .addJavadoc("@param $L 主键ID\n", id.fieldName)
        // .addJavadoc("@return 对应的记录，如果不存在则返回null\n")
        // .build();

        // interfaceBuilder.addMethod(selectByIdMethod);

        // // deleteById 方法
        // MethodSpec deleteByIdMethod = MethodSpec.methodBuilder("deleteById")
        // .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        // .returns(TypeName.INT)
        // .addParameter(idType, id.fieldName)
        // .addJavadoc("根据ID删除$L\n", entityInfo.simpleClassName)
        // .addJavadoc("@param $L 主键ID\n", id.fieldName)
        // .addJavadoc("@return 受影响的行数\n")
        // .build();

        // interfaceBuilder.addMethod(deleteByIdMethod);

        // // update 方法
        // MethodSpec updateMethod = MethodSpec.methodBuilder("update")
        // .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        // .returns(TypeName.INT)
        // .addParameter(ClassName.get(entityInfo.packageName,
        // entityInfo.simpleClassName), "entity")
        // .addJavadoc("更新$L记录\n", entityInfo.simpleClassName)
        // .addJavadoc("@param entity 要更新的实体对象\n")
        // .addJavadoc("@return 受影响的行数\n")
        // .build();

        // interfaceBuilder.addMethod(updateMethod);
        // }

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

        // 生成 Java 文件
        JavaFile javaFile = JavaFile.builder(mapperPackageName, interfaceBuilder.build())
                .indent("    ") // 使用4个空格作为缩进
                .build();

        // 写入文件
        javaFile.writeTo(processingEnv.getFiler());
    }
}
