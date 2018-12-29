package com.wangku.miaodan.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 *  字符串工具类
 * 作者：路路
 * 邮箱：lulu@99114.com
 * 描述：Strings.java
 * 日期：2018年12月22日 下午12:10:36
 */
public class Strings {
	
	/**
	 * 字符串为 null、长度为0 或者内部字符全部为 ' ' '\t' '\n' '\r' 这四类字符时返回 true
	 */
	public static boolean isBlank(String str) {
		if (Objects.isNull(str) || str.isEmpty()) {
			return true;
		}
		
		int len = str.length();
		for (int i = 0; i < len; i++) {
			switch (str.charAt(i)) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				break;
			default:
				return false;
			}
		}
		return true;		
	}
	
	/**
	 * 
	 * @param params    参数集合		
	 * @param genertKey	密钥
	 * @param targetKey	目标key
	 * @return 拼装后的 字符串"abc=123&bcd=234&cde=345"
	 * @author lulu@99114.com
	 * @time 2018年12月22日 下午12:13:38
	 */
	public static String packageSign(Map<String, String> params, String genertKey, String targetKey) {
		params.remove(targetKey);
		Set<Entry<String, String>> set = new TreeMap<String, String>(params).entrySet();
		StringBuffer sb = new StringBuffer(255);
		for (Entry<String, String> entry : set) {
			if (!isBlank(entry.getValue())) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		sb.append("key=").append(genertKey);
		return sb.toString();
	}

}
