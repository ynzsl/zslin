package com.zslin.web.controller.admin;

import com.zslin.app.model.Comment;
import com.zslin.app.service.IArticleService;
import com.zslin.app.service.ICommentService;
import com.zslin.basic.auth.annotations.AdminAuth;
import com.zslin.basic.auth.annotations.Token;
import com.zslin.basic.auth.tools.TokenTools;
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
 * Created by 钟述林 393156105@qq.com on 2016/10/17 9:12.
 */
@Controller
@RequestMapping(value = "admin/comment")
@AdminAuth(name="点评管理", orderNum=7, psn="网站管理", pentity=0, porderNum=1)
public class AdminCommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IArticleService articleService;

    /** 列表 */
    @AdminAuth(name = "点评列表", orderNum = 1, icon="icon-list")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Comment> datas = commentService.findAll(new ParamFilterTools<Comment>().buildSpecification(model, request), PageableTools.basicPage(page));
        model.addAttribute("datas", datas);
        return "admin/comment/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="回复点评", orderNum=3, type="2")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        model.addAttribute("comment", commentService.findOne(id));
        return "admin/comment/update";
    }

    @Token(flag=Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Comment comment, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Comment c = commentService.findOne(id);
            c.setContent(comment.getContent());
            c.setIsShow(comment.getIsShow());
            c.setReply(comment.getReply());
            c.setReplyDate(new Date());
            commentService.save(c);
        }
        return "redirect:/admin/comment/list";
    }

    @AdminAuth(name="删除点评", orderNum=4, type="2")
    @RequestMapping(value="delete/{id}/{artId}", method= RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id, @PathVariable Integer artId) {
        try {
            commentService.delete(id);
            articleService.updateCommentCount(artId, -1); //修改
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
