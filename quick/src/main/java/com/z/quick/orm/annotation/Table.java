package com.z.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * ******************  类说明  *********************
 * class       :  Table
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  Table注解用于在po类名与表名不一致时指定表名
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**表名*/
	String tableName();
	/**是否创建表 true:创建 默认false*/
	boolean create() default false;
}

