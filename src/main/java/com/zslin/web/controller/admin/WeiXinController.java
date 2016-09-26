package com.zslin.web.controller.admin;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.auth.annotations.AdminAuth;
import com.zslin.basic.auth.annotations.Token;
import com.zslin.basic.auth.tools.TokenTools;
import com.zslin.basic.tools.*;
import com.zslin.weixin.iservice.IWeixinConfigService;
import com.zslin.weixin.iservice.IWeixinMenuService;
import com.zslin.weixin.iservice.IWeixinUserService;
import com.zslin.weixin.iservice.WeixinMenuServiceImpl;
import com.zslin.weixin.model.WeixinConfig;
import com.zslin.weixin.model.WeixinMenu;
import com.zslin.weixin.model.WeixinUser;
import com.zslin.weixin.tools.AccessTokenTools;
import com.zslin.weixin.tools.WeixinConstant;
import com.zslin.weixin.util.BeanFactoryContext;
import com.zslin.weixin.util.WeiXinMenuTool;
import com.zslin.weixin.util.WeixinUtil;
import com.zslin.weixin.vo.Message;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
@Controller
@RequestMapping(value = "/admin/weixin")
@AdminAuth(name = "微信管理",psn = "微信管理",orderNum = 1,pentity = 0,porderNum = 1)
public class WeiXinController {

    @Autowired
    private IWeixinUserService weixinUserService;

    @Autowired
    private IWeixinConfigService weixinConfigService;

    @Autowired
    private IWeixinMenuService weixinMenuService;

    @Autowired
    private WeixinMenuServiceImpl weixinMenuServiceImpl;

    @AdminAuth(name = "微信用户列表",orderNum = 2,icon = "icon-list")
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request,Integer page){
        Page<WeixinUser> datas = weixinUserService.findAll(new ParamFilterTools<WeixinUser>().buildSpecification(model,request), PageableTools.basicPage(page));
        model.addAttribute("datas",datas);
        return "/admin/weixin/list";
    }

    @AdminAuth(name="设置用户管理权限", orderNum=9, type="2")
    @RequestMapping(value = "updateIsAdmin/{id}/{isAdmin}", method = RequestMethod.POST)
    public @ResponseBody String updateIsAdmin(@PathVariable Integer id, @PathVariable Integer isAdmin) {
        try {
            weixinUserService.updateIsAdmin(id, isAdmin);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name = "微信配置", orderNum = 3,icon = "icon-plus")
    @RequestMapping(value = "config",method = RequestMethod.GET)
    public String weixinConfig(Model model,HttpServletRequest request){
        boolean flag = false;
        if(BeanFactoryContext.getAccessToken()==null) flag =true;
        WeixinConfig weixinConfig = weixinConfigService.findOne(1);
        if(weixinConfig==null) weixinConfig = new WeixinConfig();
        model.addAttribute("weixinConfig",weixinConfig);
        model.addAttribute("flag",flag);
        return "/admin/weixin/config";
    }

    @RequestMapping(value = "config",method = RequestMethod.POST)
    public String weixinConfig(Model model,WeixinConfig weixinConfig){
        boolean flag = false;
        if(BeanFactoryContext.getAccessToken()==null) flag =true;
        weixinConfig.setId(1);
        weixinConfigService.save(weixinConfig);
        WeixinConstant.getInstance().setWeiXinConfig(weixinConfig); //修改后将数据放到内存中
        model.addAttribute("flag",flag);
        return "redirect:/admin/weixin/config";
    }

    @AdminAuth(name = "微信菜单列表",orderNum = 4,icon = "fa fa-cog")
    @RequestMapping(value = "menuList",method = RequestMethod.GET)
    public String menuList(Model model,Integer page,Integer pid,HttpServletRequest request){
//        Page<WeiXinMenu> datas = null;
        List<WeixinMenu> datas = null;
        Specifications<WeixinMenu> params = null;
        if(pid==null||"".equals(pid)){
            params = Specifications.where(new BaseSpecification<>( new SearchCriteria("pid", "isnull", "")));
        }else{
            params = Specifications.where(new BaseSpecification<>( new SearchCriteria("pid", "eq", pid)));
        }
        datas = weixinMenuService.findAll(params, SortTools.basicSort("asc", "orderNo"));
        model.addAttribute("datas",datas);
        return "/admin/weixin/menuList";
    }

    @AdminAuth(name = "添加微信菜单",orderNum = 5,icon = "icon-plus", type = "2")
    @RequestMapping(value = "addMenu",method = RequestMethod.GET)
    @Token(flag = Token.READY)
    public String addWeiXinMenu(Model model, Integer pid, HttpServletRequest request){
        model.addAttribute("weixinMenu",new WeixinMenu());
        if(pid!=null && pid>0) {
            model.addAttribute("pmenu", weixinMenuService.findOne(pid));
            model.addAttribute("pid",pid);
        }
        return "/admin/weixin/addMenu";
    }

    @RequestMapping(value = "addMenu",method = RequestMethod.POST)
    @Token(flag = Token.CHECK)
    public String addWeiXinMenu(Model model, WeixinMenu weixinMenu,Integer pid, HttpServletRequest request){
        if(TokenTools.isNoRepeat(request)){
            weixinMenu.setCreateDate(new Date());
            if(weixinMenu.getType()==null || "".equals(weixinMenu.getType())) {
                weixinMenu.setType("view");
            }
            Integer orderNo = 1;
            if(pid==null || pid<=0) {
                weixinMenu.setPid(null);
                orderNo = weixinMenuService.queryMaxOrderNo();
            } else {
                orderNo = weixinMenuService.queryMaxOrderNo(pid);
            }
            orderNo = orderNo==null||orderNo<=0?1:orderNo+1;
            weixinMenu.setOrderNo(orderNo);
            weixinMenuService.save(weixinMenu);
        }
        return "redirect:/admin/weixin/menuList";
    }

    /**
     * 生成微信菜单
     */
    @AdminAuth(name = "生成微信菜单",orderNum = 6,icon = "",type = "2")
    @RequestMapping(value = "createMenu",method = RequestMethod.POST)
    public @ResponseBody String createWeiXinMenu(Model model,HttpServletRequest request){
        String result = WeiXinMenuTool.createWeiXinMenuJson(
                AccessTokenTools.getInstance().getAccessToken(), WeixinUtil.createWeiXinMenu(weixinMenuService));
        Message meg = JSON.parseObject(result,Message.class);
        if(meg.getErrmsg().equals("ok")){
            return "1";
        }else{
            return "0";
        }
    }

    @AdminAuth(name = "更新微信菜单",orderNum = 7,icon = "",type = "2")
    @RequestMapping(value = "updateWeiXinMenu/{id}",method = RequestMethod.GET)
    public String updateWeiXinMenu(Model model, @PathVariable  Integer id,HttpServletRequest request){
        WeixinMenu weiXinMenu = weixinMenuService.findOne(id);
        model.addAttribute("weiXinMenu",weiXinMenu);
        return "/admin/weixin/updateMenu";
    }

    @RequestMapping(value = "updateWeiXinMenu/{id}",method = RequestMethod.POST)
    public String updateWeiXinMenu(Model model, @PathVariable Integer id, WeixinMenu weixinMenu,HttpServletRequest request){
        WeixinMenu m = weixinMenuService.findOne(id);
        MyBeanUtils.copyProperties(weixinMenu, m, new String[]{"id", "pid"});
        weixinMenuService.save(m);
        if(m.getPid()!=null && m.getPid()>0) {
            return "redirect:/admin/weixin/menuList?pid="+m.getPid();
        }
        return "redirect:/admin/weixin/menuList";
    }

    @AdminAuth(name="删除微信菜单", orderNum=8, type="2")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) {
        try {
            weixinMenuService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @RequestMapping("updateSort")
    @AdminAuth(name="微信菜单排序", orderNum=9, type="2")
    public @ResponseBody String updateSort(Integer[] ids) {
        try {
            weixinMenuServiceImpl.updateSort(ids);
        } catch (Exception e) {
            return "0";
        }
        return "1";
    }
}
