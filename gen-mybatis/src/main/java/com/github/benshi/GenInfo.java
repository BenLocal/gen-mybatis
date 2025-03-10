package com.github.benshi;

import javax.lang.model.element.TypeElement;

/**
 * 
 * 
 * @date 2025年3月10日
 * @time 21:43:00
 * @author tangchuanyu
 * @description
 * 
 */
public class GenInfo {
    private final TypeElement element;
    private final String packageName;
    private final String className;
    private final String simpleClassName;

    public GenInfo(TypeElement element, String packageName) {
        this.element = element;
        this.packageName = packageName;

        this.className = this.element.getQualifiedName().toString();
        this.simpleClassName = this.element.getSimpleName().toString();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }
}
