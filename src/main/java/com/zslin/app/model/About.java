package com.zslin.app.model;

import javax.persistence.*;

/**
 * Created by zsl-pc on 2016/10/8.
 */
@Entity
@Table(name = "t_about")
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 具体内容 */
    private String content;

    /** Markdown内容  */
    @Column(name = "md_content")
    private String mdContent;

    /** 浏览量 */
    @Column(name = "read_count")
    private Integer readCount;

    public Integer getId() {
        return id;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public String getContent() {
        return content;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }
}
