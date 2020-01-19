package com.youkeshu.hr.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: liuxing
 * @Date: 2018/11/5 11:32
 * @Description:  validate 只要一个字段填写就行，要实现IExcelValidate.validate(Object o); o是excel一行数据
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UploadField {

    int order();

    int[] group() default {0};

    boolean required() default false;

    String validate() default "";

}