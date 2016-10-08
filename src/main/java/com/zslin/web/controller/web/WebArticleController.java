package com.zslin.web.controller.web;

import com.zslin.app.model.Article;
import com.zslin.app.service.IArticleService;
import com.zslin.basic.tools.BaseSpecification;
import com.zslin.basic.tools.PageableTools;
import com.zslin.basic.tools.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl-pc on 2016/10/1.
 */
@Controller
@RequestMapping(value = "web/article")
public class WebArticleController {

    @Autowired
    private IArticleService articleService;

    /** 文章详情 */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    private String detail(Model model, @PathVariable Integer id, HttpServletRequest request) {
        articleService.updateReadCount(id); //修改浏览量

        Article article = articleService.findOne(id);
        model.addAttribute("article", article); //获取文章信息
        model.addAttribute("nextArt", queryOne(article.getCateId(), id, true));
        model.addAttribute("preArt", queryOne(article.getCateId(), id, false));

        return "web/article/detail";
    }

    private Article queryOne(Integer cateId, Integer id, boolean next) {
        String opt = next?"gt":"lt";
        Specifications<Article> infoParam = Specifications.where(new BaseSpecification<Article>(new SearchCriteria("cateId", "eq", cateId)));
        infoParam = infoParam.and(new BaseSpecification<>(new SearchCriteria("id", opt, id)));

        Page<Article> artList = articleService.findAll(infoParam, PageableTools.basicPage(0,1));
        return artList.getTotalElements()>=1?artList.iterator().next():null;
    }
}
