package com.zslin.weixin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by admin on 2016/9/19.
 */
@Entity
@Table(name = "t_weixin_config")
public class WeixinConfig {

    @Id
    private Integer id;
    private String appID;
    private String appsecret;
    private String Token;
    @Column(name = "aes_key")
    private String aesKey;

    /** 事件模板Id */
    @Column(name = "event_temp_id")
    private String eventTempId;


    /** 用户关注时的消息提示 */
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getEventTempId() {
        return eventTempId;
    }

    public void setEventTempId(String eventTempId) {
        this.eventTempId = eventTempId;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
