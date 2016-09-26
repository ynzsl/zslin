package com.zslin.web.controller.admin;

import com.zslin.basic.auth.annotations.AdminAuth;
import com.zslin.basic.auth.annotations.Token;
import com.zslin.basic.auth.dto.AuthToken;
import com.zslin.basic.auth.tools.TokenTools;
import com.zslin.basic.tools.PageableTools;
import com.zslin.basic.tools.ParamFilterTools;
import com.zslin.weixin.iservice.IWeixinMessageService;
import com.zslin.weixin.model.WeixinMessage;
import com.zslin.weixin.tools.WeixinXmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/26.
 */
@Controller
@RequestMapping(value = "admin/weixinMessage")
@AdminAuth(name = "微信消息管理",psn = "微信管理",orderNum = 2, pentity = 0,porderNum = 1)
public class WeixinMessageController {

    @Autowired
    private IWeixinMessageService weixinMessageService;

    @AdminAuth(name = "消息列表", orderNum = 1, icon="icon-list")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<WeixinMessage> datas = weixinMessageService.findAll(new ParamFilterTools<WeixinMessage>().buildSpecification(model, request), PageableTools.basicPage(page));
        model.addAttribute("datas", datas);
        return "admin/weixinMessage/list";
    }

    @Token(flag=Token.READY)
    @AdminAuth(name="回复消息", orderNum=3, type="2")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        WeixinMessage message = weixinMessageService.findOne(id);
        model.addAttribute("weixinMessage", message);
        return "admin/weixinMessage/update";
    }

    @Token(flag=Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, WeixinMessage weixinMessage, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            WeixinMessage message = weixinMessageService.findOne(id);
            message.setReply(weixinMessage.getReply());
            message.setReplyDate(new Date());
            AuthToken at = (AuthToken) request.getSession().getAttribute(AuthToken.SESSION_NAME);
            if(at!=null) {
                message.setReplyAuthor(at.getUser().getNickname()+"["+at.getUser().getUsername()+"]");
            }
            weixinMessageService.save(message);
            WeixinXmlUtil.eventRemind("微信消息已回复", message.getOpenid(), "微信消息", "回复内容："+weixinMessage.getReply(), null);
        }
        return "redirect:/admin/weixinMessage/list";
    }
}
