/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.monitor;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

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
	
	public static void start(String maxExecuteTimeFilePath){
		queue = new LinkedBlockingQueue<String>(128);
		new Thread(()->{
			String path = maxExecuteTimeFilePath;
			if (!maxExecuteTimeFilePath.endsWith(File.separator)) {
				path += File.separator;
			}
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
						writer = FileUtil.getPrintWriter(path + fileName + fileSuffix, StandardCharsets.UTF_8.name(), true);
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