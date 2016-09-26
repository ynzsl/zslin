package com.zslin.weixin.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by admin on 2016/9/22.
 */
public class WeiXinMenuTool {
    /**
     * https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN
     * @param weiXinMenuList
     */

    private static String createMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create";

    public static String createWeiXinMenuJson(String access_token,String json){
        String result = "";
        try {
            result = Unirest.post(createMenuUrl)
                    .queryString("access_token",access_token)
                    .body(json)
                    .asString()
                    .getBody();
        } catch (UnirestException e) {
            return result;
        }
        return result;
    }
}
