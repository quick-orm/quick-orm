package com.z.quick.orm.register;

import com.z.quick.orm.sql.builder.ISqlBuilder;
import com.z.quick.orm.sql.builder.SqlBuilderProcessor;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;
import com.z.quick.orm.sql.convert.IFieldConvert;

/**
 * ******************  类说明  *********************
 * class       :  Register
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  通用组件注册类
 * @see        :                        
 * ***********************************************
 */
public class QuickRegister {
	
	/**
	 * ********************************************
	 * method name   : registerSqlBuilder 
	 * description   : 注册sql生成器
	 * @return       : void
	 * @param        : @param sBType
	 * @param        : @param sqlBuilder
	 * modified      : zhukaipeng ,  2017年8月21日  下午6:22:32
	 * @see          : 
	 * *******************************************
	 */
	public static void registerSqlBuilder(ISqlBuilder.SBType sBType,ISqlBuilder sqlBuilder) {
		SqlBuilderProcessor.registerSqlBuilder(sBType, sqlBuilder);
	}
	/**
	 * ********************************************
	 * method name   : registerConvert 
	 * description   : 注册类型转换器（java to db, db to java）
	 * @return       : void
	 * @param        : @param clzz
	 * @param        : @param ac
	 * modified      : zhukaipeng ,  2017年8月21日  下午6:23:00
	 * @see          : 
	 * *******************************************
	 */
	public static void registerConvert(Class<?> clzz,IFieldConvert ac){
		FieldConvertProcessor.registerConvert(clzz, ac);
	}
}
