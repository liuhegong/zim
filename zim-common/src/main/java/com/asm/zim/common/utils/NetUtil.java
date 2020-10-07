package com.asm.zim.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : azhao
 * @description
 */
public class NetUtil {
	private static Logger logger = LoggerFactory.getLogger(NetUtil.class);
	
	/**
	 * 测试网络可达性
	 *
	 * @param ip
	 * @param timeout
	 * @return
	 */
	public static boolean isReachable(String ip, int timeout) {
		boolean reachable = false;
		try {
			InetAddress address = InetAddress.getByName(ip);
			reachable = address.isReachable(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reachable;
	}
	
	/**
	 * 测试网络可达性
	 *
	 * @param ip
	 * @return
	 */
	public static boolean isReachable(String ip) {
		return isReachable(ip, 2000);
	}
	
	/**
	 * 判断 是否ip 网段
	 * 10.153.48.127", "10.153.48.0/26
	 *
	 * @param network
	 * @param mask
	 * @return
	 */
	public static boolean isInnerRange(String network, String mask) {
		String[] networkIps = network.split("\\.");
		int ipAddr = (Integer.parseInt(networkIps[0]) << 24)
				| (Integer.parseInt(networkIps[1]) << 16)
				| (Integer.parseInt(networkIps[2]) << 8)
				| Integer.parseInt(networkIps[3]);
		int type = Integer.parseInt(mask.replaceAll(".*/", ""));
		int mask1 = 0xFFFFFFFF << (32 - type);
		String maskIp = mask.replaceAll("/.*", "");
		String[] maskIps = maskIp.split("\\.");
		int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24)
				| (Integer.parseInt(maskIps[1]) << 16)
				| (Integer.parseInt(maskIps[2]) << 8)
				| Integer.parseInt(maskIps[3]);
		
		return (ipAddr & mask1) == (cidrIpAddr & mask1);
	}
	
	/**
	 * 通过网段 获取指定的本机ip
	 *
	 * @param mask
	 * @return
	 */
	public static String getLocalIp(String mask) {
		List<String> list = getIpRange();
		for (String ip : list) {
			if (isInnerRange(ip, mask)) {
				return ip;
			}
		}
		return "127.0.0.1";
	}
	
	/**
	 * 获取本机所有的ip
	 *
	 * @return
	 */
	public static List<String> getIpRange() {
		List<String> list = new LinkedList<>();
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();//获取本地所有网络接口
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				if (ni.isUp()) {
					Enumeration<InetAddress> enumInetAddr = ni.getInetAddresses();
					while (enumInetAddr.hasMoreElements()) {
						InetAddress inetAddress = enumInetAddr.nextElement();
						
						boolean hasConnectIp = inetAddress instanceof Inet4Address
								&& inetAddress.isSiteLocalAddress();
						if (hasConnectIp) {
							list.add(inetAddress.getHostAddress());
						}
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			list.add("127.0.0.1");
		}
		return list;
	}
	
	public static void main(String[] args) {
		List<String> list = getIpRange();
		logger.info(String.valueOf(list));
		for (String ip : list) {
			boolean isInnerRange = isInnerRange(ip, "192.168.52.1/24");
			logger.info("{} is {}", ip, isInnerRange);
		}
	}
}
