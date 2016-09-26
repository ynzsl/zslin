package com.zslin.web.controller.web;

import com.zslin.weixin.dto.MsgDto;
import com.zslin.weixin.iservice.IWeixinConfigService;
import com.zslin.weixin.iservice.IWeixinMessageService;
import com.zslin.weixin.iservice.IWeixinUserService;
import com.zslin.weixin.model.WeixinConfig;
import com.zslin.weixin.model.WeixinMessage;
import com.zslin.weixin.model.WeixinUser;
import com.zslin.weixin.tools.*;
import com.zslin.weixin.util.SecurityKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/9/21.
 */
@Controller
@RequestMapping(value = "weixin")
public class InitWeiXinController {

    @Autowired
    private IWeixinConfigService weixinConfigService;

    @Autowired
    private IWeixinUserService weixinUserService;

    @Autowired
    private IWeixinMessageService weixinMessageService;

    /**
     * 微信服务器验证
     */
    @RequestMapping(value = "init",method = RequestMethod.GET)
    public void init(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws IOException {
        System.out.println("========"+signature);
        WeixinConfig wc = weixinConfigService.findOne(1);
        String[] arr = {wc.getToken(),timestamp,nonce};
        Arrays.sort(arr);
        StringBuffer sb = new StringBuffer();
        for(String a:arr) {
            sb.append(a);
        }
        String sha1Msg = SecurityKit.sha1(sb.toString());
        if(signature.equals(sha1Msg)) {
            response.getWriter().println(echostr);
        }
    }

    @RequestMapping(value = "init", method = RequestMethod.POST)
    public void initPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        String docSend = "";
        try {
            out = response.getWriter();
            request.setCharacterEncoding("UTF-8");
            Element root = EventTools.getMessageEle(request);

            Node fromUser = root.getElementsByTagName("FromUserName").item(0);
            Node createTime = root.getElementsByTagName("CreateTime").item(0);
            Node msgType = root.getElementsByTagName("MsgType").item(0);
            Node content = root.getElementsByTagName("Content").item(0);
            Node event = root.getElementsByTagName("Event").item(0);
            Node EventKey = root.getElementsByTagName("EventKey").item(0);
            Node msgId = root.getElementsByTagName("MsgId").item(0);

            String msgTypeStr = msgType.getTextContent(); //事件类型
            String fromOpenid = fromUser.getTextContent(); //用户的openid
            String cTime = createTime.getTextContent(); //创建时间
            String builderName = root.getElementsByTagName("ToUserName").item(0).getTextContent(); //开发者微信号
            MsgDto dto = RepeatTools.getOldMsgDto(fromOpenid, cTime); //获取对象，并存入内存中
            if(!dto.getIsNew()) {
                docSend = dto.getContent();
                out.print(docSend);
                out.flush();
                out.close();
                return;
            } else {
//                String contentStr = ""; //用户发送的消息
                if("text".equalsIgnoreCase(msgTypeStr)) {
                    WeixinMessage message = new WeixinMessage();
                    message.setContent(content.getTextContent());
                    message.setCreateTime(Integer.parseInt(createTime.getTextContent()));
                    message.setMsgId(msgId.getTextContent());
                    message.setOpenid(fromOpenid); message.setCreateDate(new Date());
                    message.setType(msgType.getTextContent());

//                    contentStr = message.getContent();
                    WeixinUser wxUser = WeixinUserTools.addOrUpdateUser(request, weixinUserService, fromOpenid); //更新用户信息
                    if(wxUser!=null) {
                        message.setNickname(wxUser.getNickName());
                    }
                    weixinMessageService.save(message);
                    List<String> adminOpenid = weixinUserService.findAdmin();
                    WeixinXmlUtil.eventRemind(adminOpenid, "微信平台有新消息", "微信消息提示", "消息内容："+content.getTextContent(), null);
                }

                if("event".equalsIgnoreCase(msgTypeStr)) { //如果是事件类型
                    if("view".equalsIgnoreCase(event.getTextContent())) {

                        //WeixinCookieTools.setFlag2Cookie(response);
                    } else if("subscribe".equalsIgnoreCase(event.getTextContent())) { //关注
                        weixinUserService.updateStatus(fromOpenid, 1);
                        WeixinUserTools.addOrUpdateUser(request, weixinUserService, fromOpenid); //添加或修改微信用户
                        docSend = WeixinXmlUtil.createTextXml(fromOpenid, builderName, WeixinConstant.getInstance().getWeiXinConfig().getHello());
//                        docSend = WeixinXmlUtil.createUrlXml(fromOpenid, builderName, "这里是标题部份", "http://www.baidu.com", "关注时的内容");
                    } else if("unsubscribe".equalsIgnoreCase(event.getTextContent())) { //取消关注
                        weixinUserService.updateStatus(fromOpenid, 2);
                    } else if("LOCATION".equalsIgnoreCase(event.getTextContent())) { //如果是获取用户地理位置
                    }
                }
            }
            out.println(docSend);
            out.flush();
            out.close();
        } catch (IOException e) {
            out.print(docSend);
            out.flush();
            out.close();
        }
    }
}
