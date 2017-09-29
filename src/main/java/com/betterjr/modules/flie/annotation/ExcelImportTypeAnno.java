package com.betterjr.modules.flie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelImportTypeAnno {

    int excelBeginRow() default 1;// 当前属性在excel中排序的位置

}
