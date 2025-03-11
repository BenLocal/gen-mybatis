package com.github.benshi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoGenColumn {
    String name();

    boolean nullable() default true;

    boolean pk() default false;

    String comment() default "";

    String type() default "";
}
