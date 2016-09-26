package com.zslin.weixin.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zsl-pc on 2016/9/26.
 */
@Entity
@Table(name = "t_weixin_message")
public class WeixinMessage {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** openid，即fromUserName */
    private String openid;

    /** 创建时间，由微信提供 */
    @Column(name = "create_time")
    private Integer createTime;

    /** 创建时间，自动生成 */
    @Column(name = "create_date")
    private Date createDate;

    /** 类型，消息类型，一般为text */
    private String type;

    /** 内容 */
    private String content;

    /** 消息Id */
    @Column(name = "msg_id")
    private String msgId;

    /** 反馈者的微信昵称 */
    private String nickname;

    /** 回复内容 */
    private String reply;

    /** 回复日期 */
    @Column(name = "reply_date")
    private Date replyDate;

    /** 是否回复 */
    @Column(name = "has_reply")
    private String hasReply;

    /** 回复者 */
    @Column(name = "reply_author")
    private String replyAuthor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public String getHasReply() {
        return hasReply;
    }

    public void setHasReply(String hasReply) {
        this.hasReply = hasReply;
    }

    public String getReplyAuthor() {
        return replyAuthor;
    }

    public void setReplyAuthor(String replyAuthor) {
        this.replyAuthor = replyAuthor;
    }
}
