package com.z.quick.orm.table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.annotation.Table;
import com.z.quick.orm.session.Session;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.builder.SqlBuilder;
import com.z.quick.orm.sql.builder.SqlBuilderProcessor;
/**
 * class       :  CreateTable
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  加载Class创建表
 * @see        :  *
 */
public class CreateTable {
	private static final Log log = LogFactory.get();
	private Session session;
	private String packagePath;
	private SqlBuilderProcessor sqlBuilderProcessor;
	
	public CreateTable(Session session,SqlBuilderProcessor sqlBuilderProcessor,String packagePath) {
		super();
		this.session = session;
		this.sqlBuilderProcessor = sqlBuilderProcessor;
		this.packagePath = packagePath;
	}

	public void start(){
		File root = new File(System.getProperty("user.dir"));
		List<String> allFileNames = getAllFileNames(root, "");
		List<String> fileNames = filterFile(allFileNames,packagePath);
		List<Class<?>> clzzs = loadClass(fileNames);
		createTable(clzzs);
	}
	
	private void createTable(List<Class<?>> clzzs){
		clzzs.forEach(clzz->{
			Table table = clzz.getAnnotation(Table.class);
			if (table == null || !table.create()) {
				return ;
			}
			log.info("准备创建表:{}",table.tableName());
			try {
				SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.CREATE_TABLE, clzz);
				log.info("建表SQL:{}",sqlInfo.getSql());
				session.update(sqlInfo.getSql(),sqlInfo.getParam());
				log.info("创建成功:{}",table.tableName());
			} catch (Exception e) {
				log.info("该表已存在:{}",table.tableName());
			}
		});
	}
	
	private List<Class<?>> loadClass(List<String> fileNames){
		List<Class<?>> clzzs = new ArrayList<>();
		fileNames.forEach(fileName->{
			try {
				clzzs.add(Thread.currentThread().getContextClassLoader().loadClass(fileName));
			} catch (Exception e) {
				log.error(e,"加载:{}出错",fileName);
			}
		});
		return clzzs;
	}
	
	private List<String> filterFile(List<String> allFileNames,String packagePath){
		List<String> fileNames = new ArrayList<String>();
		Pattern p = Pattern.compile("("+packagePath+".*).class");
		allFileNames.forEach(fileName->{
			Matcher m = p.matcher(fileName);
			if (m.find()) {
				fileName = m.group(1);
				if (!fileName.contains("$")) {
					fileNames.add(fileName);
				}
			}
		});
		return fileNames;
	}

	private List<String> getAllFileNames(File folder, String packageName){
		List<String> listFile = new ArrayList<String>();
		File[] files = folder.listFiles();
		for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
			File file = files[fileIndex];
			if (file.isDirectory()) {
				listFile.addAll(getAllFileNames(file, packageName + file.getName() + "."));
			} else {
				if (file.getName().endsWith(".class")) {
					listFile.add(packageName+file.getName());
				}
			}
		}
		return listFile;
	}

}
