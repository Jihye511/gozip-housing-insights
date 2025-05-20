package com.ssafy.local.util;

public class MyHash {
	public static String hash(String input) {
		Long hash=0L;
		for(int i=0;i<input.length();i++) {
			hash=hash*31+input.charAt(i)*(i+1);
		}
		//해시값은 기본적으로 16진수를 사용(SHA,MD5 등)
		//숫자 그대로 저장하면 너무 길어진다.
		return Long.toHexString(hash);	
	}
}