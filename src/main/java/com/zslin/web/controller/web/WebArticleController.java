package com.zslin.web.controller.web;

import com.zslin.app.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

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
        model.addAttribute("article", articleService.findOne(id)); //获取文章信息
        return "web/article/detail";
    }
}
