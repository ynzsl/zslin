package com.zslin.weixin.tools;

import com.zslin.weixin.iservice.IWeixinUserService;
import com.zslin.weixin.model.WeixinUser;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/26.
 */
public class WeixinUserTools {

    public static String getOpenid(HttpServletRequest request) {
        if(request==null) {return null;}
        String openid = request.getParameter("openid"); //先获取地址栏中的参数
        if(openid==null || "".equals(openid) || "null".equalsIgnoreCase(openid)) {
            openid = (String) request.getSession().getAttribute("openid");
        }
        return openid;
    }

    public static final String LOGIN_WEIXIN_USER = "login_weixin_user";

    public static WeixinUser addOrUpdateUser(HttpServletRequest request, IWeixinUserService weixinService, String openid) {
        WeixinUser user = (WeixinUser) request.getSession().getAttribute(LOGIN_WEIXIN_USER); //先获取Session中的用户对象
        if(user!=null) {return user;}
        if(openid==null || "".equals(openid)) {return null;}
        user = weixinService.findByOpenId(openid);
        if(user==null) {user = new WeixinUser(); user.setIsAdmin(0); user.setBindingDate(new Date());}
        user.setBindingStatus(1);
        user.setOpenId(openid);

        JSONObject jsonObj = WeixinConstant.getUserInfo(openid);
        if(jsonObj!=null) {
            String jsonStr = jsonObj.toString();
            if(jsonStr.indexOf("errcode")<0 && jsonStr.indexOf("errmsg")<0) {
                String nickname = "";
                try {
                    nickname = jsonObj.getString("nickname");
                } catch (Exception e) {
                }
                user.setCity(jsonObj.getString("city"));
//                user.setCountry(jsonObj.getString("country"));
                user.setHeadImg(jsonObj.getString("headimgurl"));
                user.setNickName(nickname);
                user.setPlace(jsonObj.getString("province"));
//                user.setRemark(jsonObj.getString("remark"));
//			friend.setPhone(phone);
                user.setSex(jsonObj.getInt("sex"));
            }
        }

        weixinService.save(user);
        request.getSession().setAttribute(LOGIN_WEIXIN_USER, user); //将用户对象存入Session中
        return user;
    }
}
