package com.z.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ******************  类说明  *********************
 * class       :  Condition
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  在po属性上添加此标签后，该属性会作为判断条件出现在where后
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {
}
