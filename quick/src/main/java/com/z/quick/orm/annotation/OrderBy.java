package com.z.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ******************  类说明  *********************
 * class       :  OrderBy
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  在po字段上添加该注解，则该字段会出现在order by语句中
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
	/**排序方式，升序/降序，默认降序*/
	OrderByType value() default OrderByType.desc;
}

