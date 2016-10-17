package com.zslin.app.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/27.
 */
@Entity
@Table(name = "t_article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 标题 */
    private String title;

    /** 内容 */
    @Lob
    private String content;

    /** markdown的内容 */
    @Lob
    private String mdContent;

    /** 导读 */
    @Lob
    private String guide;

    @Column(name = "create_date")
    private Date createDate;

    /** 所在分类Id */
    @Column(name = "cate_id")
    private Integer cateId;

    /** 所在分类名称 */
    @Column(name = "cate_name")
    private String cateName;

    /** 阅读次数 */
    @Column(name = "read_count")
    private Integer readCount;

    /** 标签 */
    private String tags;

    /** 是否前台显示 */
    @Column(name = "is_show")
    private Integer isShow;

    /** 图片路径 */
    @Column(name = "pic_path")
    private String picPath;

    /** 点评次数 */
    @Column(name = "comment_count")
    private Integer commentCount;

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
