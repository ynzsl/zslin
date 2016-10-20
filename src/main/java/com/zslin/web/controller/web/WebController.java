package com.zslin.web.controller.web;

import com.zslin.app.model.Article;
import com.zslin.app.service.IAboutService;
import com.zslin.app.service.IArticleService;
import com.zslin.basic.tools.BaseSpecification;
import com.zslin.basic.tools.PageableTools;
import com.zslin.basic.tools.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zsl-pc on 2016/9/28.
 */
@Controller
public class WebController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IAboutService aboutService;

    /** 网站首页 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Integer userId, Integer cateId, String tag, String condition, Integer page) {
        Specifications<Article> spe = null;
        if(userId!=null && userId>0) {
            spe = Specifications.where(new BaseSpecification<>(new SearchCriteria("userId", "eq", userId)));
        } else if(cateId!=null && cateId>0) {
            spe = Specifications.where(new BaseSpecification<>(new SearchCriteria("cateId", "eq", cateId)));
        } else if(tag!=null && !"".equals(tag.trim())) {
            spe = Specifications.where(new BaseSpecification<>(new SearchCriteria("tags", "like", tag)));
        }
        if(condition!=null && !"".equalsIgnoreCase(condition)) {
            spe = Specifications.where(new BaseSpecification<>(new SearchCriteria("title", "like", condition)));
            spe = spe.or(new BaseSpecification<>(new SearchCriteria("mdContent", "like", condition)));
        }
        Page<Article> datas = articleService.findAll(spe, PageableTools.basicPage(page, "desc", "createDate"));
        model.addAttribute("datas", datas);
        return "web/index";
    }

    /** 关于 */
    @RequestMapping(value = "about", method = RequestMethod.GET)
    public String about(Model model) {
        model.addAttribute("about", aboutService.findOne(1));
        return "web/about";
    }
}
