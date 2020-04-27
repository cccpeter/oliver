package com.oliver.cloud.dto;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE})
/**
 * @Author: cpeter
 * @Desc: 元数据接口
 * @Date: cretead in 2019/10/17 18:07
 * @Last Modified: by
 * @return value
 */
public @interface MetaData {
    String value();

    String tooltips() default "";

    String comments() default "";

    boolean comparable() default true;

    boolean editable() default true;

    long autoIncrementInitValue() default 0L;
}

