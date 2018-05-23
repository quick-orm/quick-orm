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

package kim.zkp.quick.orm.register;

import kim.zkp.quick.orm.sql.convert.FieldConvert;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

/**
 * class       :  Register
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  通用组件注册类
 */
public class QuickRegister {
	
	/**
	 * method name   : registerConvert 
	 * description   : 注册类型转换器（java to db, db to java）
	 * @return       : void
	 * @param        : @param clzz
	 * @param        : @param ac
	 * modified      : zhukaipeng ,  2017年8月21日 
	 */
	public static void registerConvert(Class<?> clzz,FieldConvert ac){
		FieldConvertProcessor.registerConvert(clzz, ac);
	}
}