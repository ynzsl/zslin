package com.zslin.weixin.util;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.zslin.weixin.iservice.IWeixinConfigService;
import com.zslin.weixin.iservice.IWeixinMenuService;
import com.zslin.weixin.model.WeixinConfig;
import com.zslin.weixin.model.WeixinMenu;
import com.zslin.weixin.vo.Button;
import com.zslin.weixin.vo.Sub_button;
import com.zslin.weixin.vo.WeiXinMenuDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/19.
 */
public class WeixinUtil {

    /**
     *获取access_token
     *http请求方式: GET
     *https://api.weixin.qq.com/cgi-bin/token?APPID&secret=APPSECRET
     */
    public static String getWeiXinAccessToken(IWeixinConfigService weiXinConfigService){
        WeixinConfig weiXinConfig = weiXinConfigService.findOne(1);
        String token ="";
        if(weiXinConfig!=null){
            try {
                token = Unirest.get("https://api.weixin.qq.com/cgi-bin/token")
                        .queryString("grant_type","client_credential")
                        .queryString("appid",weiXinConfig.getAppID())
                        .queryString("secret",weiXinConfig.getAppsecret()).asString().getBody();
            } catch (UnirestException e) {
                token = e.getMessage();
            }
        }
        return token;
    }

    public static String createWeiXinMenu(IWeixinMenuService weiXinMenuService){
        WeiXinMenuDto wmd = new WeiXinMenuDto();
        List<Button> buttons = new ArrayList<>();
        List<WeixinMenu> parentMenu = weiXinMenuService.findAllPidIsNull();
        for(WeixinMenu wm: parentMenu){
            List<WeixinMenu> sonMenu = weiXinMenuService.findSonMenuByPid(wm.getId());
            if(sonMenu.size()>0){
                List<Sub_button> sub_buttons = new ArrayList<>();
                Button button = new Button();
                for(WeixinMenu wxm : sonMenu){
                    Sub_button sub_button = new Sub_button();
                    sub_button.setUrl(wxm.getUrl());
                    sub_button.setType(wxm.getType());
                    sub_button.setName(wxm.getName());
                    sub_buttons.add(sub_button);
                }
                button.setName(wm.getName());
                button.setSub_button(sub_buttons);
                buttons.add(button);
            }else{
                Button button = new Button();
                button.setType(wm.getType());
                button.setName(wm.getName());
                button.setUrl(wm.getUrl());
                buttons.add(button);
            }
        }
        wmd.setButton(buttons);
        String jsonObj = JSON.toJSONString(wmd);
        return  jsonObj;
    }
}
