package kim.zkp.quick.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ******************  类说明  *********************
 * class       :  NoFind
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  PrimaryKey主要用于po更新时指定更新条件用(可指定多个)，如果在字段上添加该注解，则在更新语句中，此字段会出现在where语句后
 * @see        :                        
 * ***********************************************
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {

}
