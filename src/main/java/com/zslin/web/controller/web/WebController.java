package com.zslin.web.controller.web;

import com.zslin.app.model.Article;
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

    /** 网站首页 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Integer cateId, Integer page) {
        Specification<Article> spe = null;
        if(cateId!=null && cateId>0) {
            spe = Specifications.where(new BaseSpecification<>(new SearchCriteria("cateId", "eq", cateId)));
        }
        Page<Article> datas = articleService.findAll(spe, PageableTools.basicPage(page, "desc", "createDate"));
        model.addAttribute("datas", datas);
        return "web/index";
    }
}
