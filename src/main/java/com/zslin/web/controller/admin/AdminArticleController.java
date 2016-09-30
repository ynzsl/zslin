package com.zslin.web.controller.admin;

import com.zslin.app.model.Article;
import com.zslin.app.model.Category;
import com.zslin.app.model.Tag;
import com.zslin.app.service.IArticleService;
import com.zslin.app.service.ICategoryService;
import com.zslin.app.service.ITagService;
import com.zslin.app.tools.HtmlRegexpTools;
import com.zslin.basic.auth.annotations.AdminAuth;
import com.zslin.basic.auth.annotations.Token;
import com.zslin.basic.auth.tools.TokenTools;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.PageableTools;
import com.zslin.basic.tools.ParamFilterTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/27.
 */
@Controller
@RequestMapping(value = "admin/article")
@AdminAuth(name="文章管理", orderNum=1, psn="网站管理", pentity=0, porderNum=1)
public class AdminArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ITagService tagService;

    /** 列表 */
    @AdminAuth(name = "文章列表", orderNum = 1, icon="icon-list")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Article> datas = articleService.findAll(new ParamFilterTools<Article>().buildSpecification(model, request), PageableTools.basicPage(page));
        model.addAttribute("datas", datas);
        return "admin/article/list";
    }

    @Token(flag=Token.READY)
    @AdminAuth(name = "添加文章", orderNum = 2, icon="icon-plus")
    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        Article article = new Article();
        article.setIsShow(1);
        model.addAttribute("article", article);
        model.addAttribute("cateList", categoryService.findAll());
        return "admin/article/add";
    }

    /** 添加POST */
    @Token(flag=Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Article article, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            if(article.getGuide()==null || "".equals(article.getGuide().trim())) {
                String guide = HtmlRegexpTools.filterHtml(article.getContent()).replace("[TOCM]","").replace("[TOC]", "");
                if(guide.length()>200) {guide = guide.substring(0, 200) + "……";}
                article.setGuide(guide);
            }
            article.setReadCount(0);
            article.setCreateDate(new Date());
            addOrUpdateTags(article.getTags());
            articleService.save(article);
        }
        return "redirect:/admin/article/list";
    }

    private void addOrUpdateTags(String tags) {
        String [] tagArray = tags.split(",");
        for(String tag : tagArray) {
            if(tag==null || "".equals(tag.trim())) {continue;}
            System.out.println("======"+tag);
            Tag t = tagService.findByName(tag);
            if(t==null) {
                t = new Tag();
                t.setName(tag);
                tagService.save(t);
            }
        }
    }

    @Token(flag=Token.READY)
    @AdminAuth(name="修改文章", orderNum=3, type="2")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        model.addAttribute("article", articleService.findOne(id));
        model.addAttribute("cateList", categoryService.findAll());
        return "admin/article/update";
    }

    @Token(flag=Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Article article, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Article art = articleService.findOne(id);
            MyBeanUtils.copyProperties(article, art, new String[]{"id","readCount", "createDate"});
            if(art.getGuide()==null || "".equals(art.getGuide().trim())) {
                String guide = HtmlRegexpTools.filterHtml(art.getContent()).replace("[TOCM]","").replace("[TOC]", "");
                if(guide.length()>200) {guide = guide.substring(0, 200) + "……";}
                guide = guide.replaceAll("\n", "&nbsp;&nbsp;");
                System.out.println(guide);
                art.setGuide(guide);
            }
            articleService.save(art);
            addOrUpdateTags(art.getTags());
        }
        return "redirect:/admin/article/list";
    }

    @AdminAuth(name="删除文章", orderNum=4, type="2")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            articleService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
