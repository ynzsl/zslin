package com.zslin.web.controller.web;

import com.zslin.app.model.Article;
import com.zslin.app.model.Comment;
import com.zslin.app.service.IArticleService;
import com.zslin.app.service.ICommentService;
import com.zslin.app.tools.MailTools;
import com.zslin.basic.model.AppConfig;
import com.zslin.basic.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl-pc on 2016/10/1.
 */
@Controller
@RequestMapping(value = "web/article")
public class WebArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private MailTools mailTools;

    /** 文章详情 */
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(Model model, @PathVariable Integer id, HttpServletRequest request) {
        articleService.updateReadCount(id); //修改浏览量

        Article article = articleService.findOne(id);
        model.addAttribute("article", article); //获取文章信息
        model.addAttribute("nextArt", queryOne(article.getCateId(), id, true));
        model.addAttribute("preArt", queryOne(article.getCateId(), id, false));

        Page<Comment> comments = commentService.findAll(id, PageableTools.basicPage(0, 30));
        model.addAttribute("comments", comments);

        return "web/article/detail";
    }

    private Article queryOne(Integer cateId, Integer id, boolean next) {
        String opt = next?"gt":"lt";
        Specifications<Article> infoParam = Specifications.where(new BaseSpecification<Article>(new SearchCriteria("cateId", "eq", cateId)));
        infoParam = infoParam.and(new BaseSpecification<>(new SearchCriteria("id", opt, id)));

        Page<Article> artList = articleService.findAll(infoParam, PageableTools.basicPage(0,1));
        return artList.getTotalElements()>=1?artList.iterator().next():null;
    }

    /** 点评文章 */
    @RequestMapping(value="addComment", method= RequestMethod.POST)
    public @ResponseBody String addComment(Integer artId, String artTitle, String content, HttpServletRequest request) {
        try {
            Comment comment = new Comment();
            comment.setIsShow(1);
            comment.setContent(content);
            comment.setArtId(artId);
            comment.setArtTitle(artTitle);
            comment.setCreateDate(new Date());
            commentService.save(comment);

            articleService.updateCommentCount(artId, 1); //修改文章的点评数量

            AppConfig appConfig = (AppConfig) request.getSession().getAttribute("appConfig");
            //要有管理员邮箱才能发邮件
            if(appConfig!=null && appConfig.getAdminEmail()!=null && !"".equalsIgnoreCase(appConfig.getAdminEmail())) {
                StringBuffer sb = new StringBuffer();
                sb.append("<p>文章标题：").append(artTitle).append("</p>")
                  .append("<p>点评内容：").append(content).append("</p>")
                  .append("<p style='text-align:right; width:100%;'>").append(NormalTools.curDate()).append("</p>")
                  .append("<p style='text-align:right; width:100%;'>").append(appConfig.getAppName()).append("</p>");

                String [] emails = appConfig.getAdminEmail().split(",");
                mailTools.send("新点评 - "+appConfig.getAppName(), sb.toString(), emails);
            }

            return "1";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
