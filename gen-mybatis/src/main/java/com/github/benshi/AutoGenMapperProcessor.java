package com.github.benshi;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("com.github.benshi.AutoGenMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoGenMapperProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                System.out.println("AutoGenMapperProcessor: " + element);
                processingEnv.getMessager().printMessage(
                        javax.tools.Diagnostic.Kind.NOTE, "AutoGenMapperProcessor: " + element);
            }
        }
        return true;
    }

}
