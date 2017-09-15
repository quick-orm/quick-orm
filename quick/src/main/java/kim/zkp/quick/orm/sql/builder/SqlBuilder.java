package kim.zkp.quick.orm.sql.builder;

import kim.zkp.quick.orm.sql.SqlInfo;

/**
 * ******************  类说明  *********************
 * class       :  ISqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  sql生成器接口
 * @see        :                        
 * ***********************************************
 */
public interface SqlBuilder {
	
	enum SBType{
		SAVE,
		DELETE,
		UPDATE,
		GET,
		LIST,
		PAGE_COUNT,
		PAGE_LIST,
		CREATE_TABLE
	}
	
	SqlInfo builderSql(Object o);
	
	
	
}
