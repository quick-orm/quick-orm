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

package kim.zkp.quick.orm.session;
/**
 * class       :  Transaction
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description : 事务管理 
 * @see        :  *
 */
public interface Transaction {
	/**
	 * method name   : start 
	 * description   : 开启事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void start();
	/**
	 * method name   : rollback 
	 * description   : 回滚事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void rollback();
	/**
	 * method name   : commit 
	 * description   : 提交事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void commit();
	/**
	 * method name   : close 
	 * description   : 关闭事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void close();
}