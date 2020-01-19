package com.youkeshu.hr.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: liuxing
 * @Date: 2018/8/15 9:51
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DownloadField {

    String name();

    int[] order();

    String valueMethod() default "";

    String[] group() default {DownloadFieldAnnotationParse.DEFAULT_GROUP};

}