package com.zslin.weixin.tools;

import com.zslin.weixin.model.WeixinConfig;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zslin.weixin.util.BeanFactoryContext.getAccessToken;

/**
 * Created by zsl-pc on 2016/9/26.
 */
public class WeixinConstant {

    private static WeixinConstant instance;
    private WeixinConstant() {}

    public static WeixinConstant getInstance() {
        if(instance==null) {instance = new WeixinConstant();}
        return instance;
    }

    private WeixinConfig weixinConfig;

    public WeixinConfig getWeiXinConfig() {
        try {
            return this.weixinConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setWeiXinConfig(WeixinConfig weixinConfig) {
        this.weixinConfig = weixinConfig;
    }

    /**
     * 微信的相关交互URL
     * @author zsl
     *
     */
    public static class WeixinUrl {
        public static final String WEIXIN_ROOT_URL = "https://api.weixin.qq.com/cgi-bin/"; //微信的根Url
        public static final String GET_TOKEN = WEIXIN_ROOT_URL + "token"; //获取token的URL
        public static final String SEND_MESSAGE = WEIXIN_ROOT_URL + "message/custom/send"; //发送消息
        public static final String CREATE_MENU = WEIXIN_ROOT_URL + "menu/create"; //创建菜单
    }

    public static JSONObject getUserInfo(String openid) {
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("access_token", getAccessToken());
            params.put("openid", openid);
            params.put("lang", "zh_CN");

            String result = InternetTools.doGet("https://api.weixin.qq.com/cgi-bin/user/info", params);

            JSONObject jsonObj = new JSONObject(result);
            return jsonObj;
        } catch (Exception e) {
            String result = InternetTools.doGet("https://api.weixin.qq.com/sns/userinfo", params);
            JSONObject jsonObj = new JSONObject(result);
            return jsonObj;
        }
    }
}
