package com.wangku.miaodan.utils;

import org.apache.commons.codec.binary.Base64;



public class Base64Utils {
	
	private static Base64 base64 = new Base64();
	
	public static final String decode(final String str) {
		return new String(base64.decode(str.getBytes()));
	}
	
	public static final String encode(final String base) {
		return new String(base64.encode(base.getBytes()));
	}
	

}
