package com.zslin.basic.interceptor;

import com.zslin.app.model.Category;
import com.zslin.app.service.ICategoryService;
import com.zslin.basic.iservice.IAppConfigService;
import com.zslin.basic.model.AppConfig;
import com.zslin.basic.tools.SortTools;
import com.zslin.weixin.iservice.IWeixinConfigService;
import com.zslin.weixin.model.WeixinConfig;
import com.zslin.weixin.tools.WeixinConstant;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Configuration
public class SystemInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IAppConfigService appConfigService;

    @Autowired
    private IWeixinConfigService weiXinConfigService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //将系统配置文件存入Session中
        AppConfig appConfig = (AppConfig) session.getAttribute("appConfig");
        if(appConfigService==null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            appConfigService = (IAppConfigService) factory.getBean("appConfigService");
        }
        if(appConfig==null) {
            appConfig = appConfigService.loadOne();
            session.setAttribute("appConfig", appConfig);
        }

        List<Category> cateList = (List<Category>) session.getAttribute("navCateList");
        if(cateList==null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            categoryService = (ICategoryService) factory.getBean("categoryService");
            cateList = categoryService.findNavCate(SortTools.basicSort("asc", "orderNo"));
            session.setAttribute("navCateList", cateList);
        }

        //在生成菜单时需要用到该配置文件，所以要在这里初始化
        WeixinConfig weiXinConfig = (WeixinConfig) session.getAttribute("weiXinConfig");
        if(weiXinConfig==null) {
            weiXinConfig = WeixinConstant.getInstance().getWeiXinConfig();
        }
        if(weiXinConfig==null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            weiXinConfigService = (IWeixinConfigService) factory.getBean("weiXinConfigService");

            weiXinConfig = weiXinConfigService.findOne(1);
            session.setAttribute("weiXinConfig", weiXinConfig);
            WeixinConstant.getInstance().setWeiXinConfig(weiXinConfig);
        }
        return super.preHandle(request, response, handler);
    }
}