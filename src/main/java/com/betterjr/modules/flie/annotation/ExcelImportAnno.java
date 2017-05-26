package com.betterjr.modules.flie.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelImportAnno {
	
	String isMust() default "0"; // 属性导入是否必须有值 1 必须  0 不必须
	
	String cloumnType() default "0"; //当前列的类型0 字符串   1 数字
	
	String cloumnChineseName() default "" ;//列的中文名称
	
	int cloumnOrder() ;//当前属性在excel中排序的位置
	
	String vailedRegular() default "";//校验字符串正则表达式

}
