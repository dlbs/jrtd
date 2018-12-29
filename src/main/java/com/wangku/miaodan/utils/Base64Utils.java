package com.wangku.miaodan.utils;

import java.util.Base64;


public class Base64Utils {
	
	public static final String decode(final String str) {
		return new String(Base64.getDecoder().decode(str.getBytes()));
	}
	
	public static final String encode(final String base) {
		return new String(Base64.getEncoder().encode(base.getBytes()));
	}

}
