package com.wangku.miaodan.utils;

import java.util.Objects;

public class Strings {
	
	public static boolean isNullOrEmpty(String str) {
		return Objects.isNull(str) || str.isEmpty();
	}

}
