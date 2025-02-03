package com.springboot.biz.util;

public class CustomJWTException extends RuntimeException{

	public CustomJWTException(String msg) {
		super(msg);
	}
}
