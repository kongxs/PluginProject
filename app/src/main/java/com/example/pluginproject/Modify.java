package com.example.pluginproject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author
 * @date 2021-06-02 10:34
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
public @interface Modify {
    String value() default "";
}
