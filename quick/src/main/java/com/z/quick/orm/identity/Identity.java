package com.z.quick.orm.identity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

	public static String nextId(String key) {
		Identity i = identityCache.get(key);
		if (i == null) {
			i = new Identity();
			identityCache.put(key, i);
		}
		return i.get();
	}
	
	public static String nextId() {
		return nextId("default");
	}
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			System.out.println(nextId());
		}
	}
	
}
