package com.zslin.weixin.tools;

import com.zslin.weixin.model.WeixinConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zsl
 *
 */
public class AccessTokenTools {

	private static AccessTokenTools instance;
	private static String ACCESS_TOKEN;
	
	public static AccessTokenTools getInstance() {
		if(instance==null) {
			instance = new AccessTokenTools();
		}
		return instance;
	}
	
	private AccessTokenTools(){}
	
	public String getAccessToken() {
		if(ACCESS_TOKEN==null || "".equals(ACCESS_TOKEN)) {
			String newToken = getNewAccessToken();
			if(newToken==null || "".equals(newToken) || "null".equalsIgnoreCase(newToken)) {
				ACCESS_TOKEN = "0,0";
			} else { ACCESS_TOKEN = System.currentTimeMillis()+","+newToken; }
		} else {
			String [] con_array = ACCESS_TOKEN.split(",");
			Long oldTime = Long.parseLong(con_array[0]); //时间
			String res = con_array[1];
			Long newTime = System.currentTimeMillis();
			int min = (int) ((newTime-oldTime)/1000/60); //分
			if(min>=100 || "".equals(res.trim()) || "null".equals(res.trim())) { //与上次获取的时间相差100分钟，则重新获取
				String newToken = getNewAccessToken();
				if(newToken==null || "".equals(newToken) || "null".equalsIgnoreCase(newToken)) {
					ACCESS_TOKEN = "0,0";
				} else { ACCESS_TOKEN = System.currentTimeMillis()+","+newToken; }
			}
		}
		String [] con_array = ACCESS_TOKEN.split(",");
		return con_array[1];
	}
	
	/** 获取新的AccessToken */
	private String getNewAccessToken() {
		WeixinConfig wxConfig = WeixinConstant.getInstance().getWeiXinConfig();
		Map<String, Object> params = new HashMap<>();
		params.put("grant_type", "client_credential");
		params.put("appid", wxConfig.getAppID());
		params.put("secret", wxConfig.getAppsecret());
		String result = InternetTools.doGet(WeixinConstant.WeixinUrl.GET_TOKEN, params);
		if(result!=null && result.indexOf("access_token")<0) { //如果没有获取出来再获取一次
//			System.out.println("AccessTokenTools.getNewAccessToken========reload 2 ");
			result = InternetTools.doGet(WeixinConstant.WeixinUrl.GET_TOKEN, params);
		}
		if(result==null) {return "";}
		return JsonTools.getJsonParam(result, "access_token");
	}
	
	/** 定时刷新AccessToken所用的方法 */
	public String refreshAccessToken() {
		String token = "";
		int flag = 0;
		while((token==null || "".equals(token)) && flag++<10) {
			token = getNewAccessToken();
		}
		if(token==null || "".equals(token)) {
			ACCESS_TOKEN = "0,0";
		} else {
			ACCESS_TOKEN = System.currentTimeMillis()+","+token;
		}
		return token;
	}
}
