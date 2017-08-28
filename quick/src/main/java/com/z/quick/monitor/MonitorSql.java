package com.z.quick.monitor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.Session;

/**
 * ******************  类说明  *********************
 * class       :  MonitorSql
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ***********************************************
 */
public class MonitorSql {
	private static final Log log = LogFactory.get();
	private static LinkedBlockingQueue<String> queue ;
	private static final String contentModel = "Execute SQL:%s; TIME:%smm;";
	private static final String fileSuffix = ".txt";
	
	public static void record(String sql,long executeTime){
		try {
			queue.put(String.format(contentModel, sql,executeTime));
		} catch (InterruptedException e) {
			log.error("Put timeout sql loga error",e);
		}
	}
	
	public static void start(){
		queue = new LinkedBlockingQueue<String>(128);
		new Thread(()->{
			long size = 0;
			PrintWriter writer = null;
			String fileName = null;
			log.info("===============Start Sql Monitor===============");
			while(true){
				try {
					String today = DateUtil.today();
					if (!today.equals(fileName)) {
						if (writer != null) {
							writer.flush();
							writer.close();
						}
						fileName = today;
						String filePath = Session.getSession().getJdbcConfig().getMaxExecuteTimeFilePath();
						writer = FileUtil.getPrintWriter(filePath + fileName + fileSuffix, StandardCharsets.UTF_8.name(), true);
					}
					String sql = queue.take();
					writer.println(sql);
					if ((size++)%5==0) {
						writer.flush();
					};
				} catch (InterruptedException e) {
					try {
						//防止队列出错，造成死循环，占用cpu资源
						Thread.sleep(2*1000);
					} catch (InterruptedException e1) {
						log.error("Get timeout sql loga error",e);
					}
				}catch (Exception e) {
					log.error("Writer timeout sql error",e);
				}
			}
		}).start();
	}
	
}
