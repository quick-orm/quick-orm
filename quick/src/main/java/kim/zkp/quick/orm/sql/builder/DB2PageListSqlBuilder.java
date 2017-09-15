package kim.zkp.quick.orm.sql.builder;

import java.util.Map;

import kim.zkp.quick.orm.model.Page;
import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  GetSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成get SQL
 * @see        :                        
 * ***********************************************
 */
public class DB2PageListSqlBuilder extends AbstractSqlBuilder {
	private static final String PAGE_LIST_TEMPLATE = "select * from ( select row_.*, rownumber() over() as rownumber_ from (#listSql)as row_ ) as t where rownumber_ > #start and rownumber_ <= #end ";
	private SqlBuilder listBuilder;
	
	public DB2PageListSqlBuilder(SqlBuilder listBuilder) {
		super();
		this.listBuilder = listBuilder;
	}



	@Override
	public SqlInfo builderSql(Object o) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		SqlInfo sqlInfo = listBuilder.builderSql(o);
		String sql = sqlInfo.getSql();
		Integer pageNum = pageInfo.get("pageNum");
		Integer pageSize = pageInfo.get("pageSize");
		Integer start = (pageNum-1)*pageSize;
		Integer end = pageNum*pageSize;
		sql = PAGE_LIST_TEMPLATE.replace("#listSql", sql);
		sql = sql.replace("#start", start.toString());
		sql = sql.replace("#end", end.toString());
		sqlInfo.setSql(sql);
		
		return sqlInfo;
	}
	
}




