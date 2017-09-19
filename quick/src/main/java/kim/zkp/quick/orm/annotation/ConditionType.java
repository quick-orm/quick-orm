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

package kim.zkp.quick.orm.annotation;

public enum ConditionType{
	/**小于*/
	LT(" < "),
	/**大于*/
	GT(" > "),
	/**小于等于*/
	LE(" <= "),
	/**大于等于*/
	GE(" >= "),
	/**等于*/
	EQ(" = "),
	/**不等于*/
	NEQ(" != "),
	/**like条件*/
	LIKE(" like ");
	
	private String operation;
	private ConditionType(String operation){
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
	
}