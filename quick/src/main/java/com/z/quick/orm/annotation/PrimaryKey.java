package com.z.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ******************  类说明  *********************
 * class       :  NoFind
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  在po属性上添加此标签，表示该属性为数据库主键，在调用get方法时，会将该属性作为查询条件
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {

}
