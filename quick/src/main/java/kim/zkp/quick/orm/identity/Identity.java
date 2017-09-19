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

package kim.zkp.quick.orm.identity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * class       :  Identity
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  主键生成器
 * @see        :  *
 */
public class Identity {

	private static Map<String, Identity> identityCache = new HashMap<String, Identity>();
	private final AtomicInteger count = new AtomicInteger(0);
	private String prefix = "000000000000";
	private String ipAddress;

	private Identity() {
		ipAddress = getIpAddress();
	}

	private synchronized String get() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String hours = sdf.format(System.currentTimeMillis());
		int value = 0;
		if (!prefix.equals(hours)) {
			prefix = hours;
			count.set(0);
		} else {
			value = count.incrementAndGet();
		}
		return prefix + ipAddress + String.format("%07d", value);
	}

	private String getIpAddress() {
		String defaultIpAddress = "128";
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return defaultIpAddress;
		}
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					try {
						String ipAddress = ip.getHostAddress();
						String[] nums = ipAddress.split("\\.");
						int ipIdentity = 0;
						for (String string : nums) {
							ipIdentity += Integer.parseInt(string);
						}
						return String .valueOf(ipIdentity);
					} catch (Exception e) {
						return defaultIpAddress;
					}
				}
			}
		}
		return defaultIpAddress;
	}
	/**
	 * method name   : nextId 
	 * description   : 获取主键 
	 * @return       : String
	 * @param        : @param key 主键标识
	 * @param        : @return 返回值格式:yyyyMMddHHmm+3位主机号+7位递增序列
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public static String nextId(String key) {
		Identity i = identityCache.get(key);
		if (i == null) {
			i = new Identity();
			identityCache.put(key, i);
		}
		return i.get();
	}
	/**
	 * method name   : nextId 
	 * description   : 获取主键，默认标识为default
	 * @return       : String
	 * @param        : @return 返回值格式:yyyyMMddHHmm+3位主机号+7位递增序列
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public static String nextId() {
		return nextId("default");
	}
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			System.out.println(nextId());
		}
	}
	
}