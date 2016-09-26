package com.zslin.weixin.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2016/9/20.
 */
@Entity
@Table(name = "t_weixin_menu")
public class WeixinMenu {

    /**
     *
     参数	是否必须	说明
     button	是	一级菜单数组，个数应为1~3个
     sub_button	否	二级菜单数组，个数应为1~5个
     type	是	菜单的响应动作类型
     name	是	菜单标题，不超过16个字节，子菜单不超过60个字节
     key	click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节
     url	view类型必须	网页链接，用户点击菜单可打开链接，不超过1024字节
     media_id	media_id类型和view_limited类型必须	调用新增永久素材接口返回的合法media_id
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer pid;

    private String type;

    @Column(name = "w_key")
    private String keyStr;

    private String url;

    private String media_id;

    private Date createDate;

    @Column(name = "order_no")
    private Integer orderNo;

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
