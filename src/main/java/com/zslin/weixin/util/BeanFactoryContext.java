package com.zslin.weixin.util;

import org.springframework.web.context.WebApplicationContext;

public class BeanFactoryContext {

	private static WebApplicationContext wc;
	private static String accessToken;
	
	public static WebApplicationContext getWc() {
		return wc;
	}
	
	public static String getAccessToken() {
		return accessToken;
	}
	
	public static void setAccessToken(String accessToken) {
		BeanFactoryContext.accessToken = accessToken;
	}

	public static void setWc(WebApplicationContext wc) {
		BeanFactoryContext.wc = wc;
	}
	
	public static Object getService(String serviceName) {
		return wc.getBean(serviceName);
	}
}
