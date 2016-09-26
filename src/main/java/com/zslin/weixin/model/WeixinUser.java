package com.zslin.weixin.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2016/9/18.
 */
@Entity
@Table(name = "t_weixin_user")
public class WeixinUser {
    /*
     具体字段有：
     openid,头像，昵称，所在城市，地址、性别，年龄，手机，用户名，真实姓名，密码，绑定状态,关注时间,绑定时间,创建时间
    */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * openId
     */
    private String openId;
    /**
     *头像
     */
    private String headImg;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 地址
     */
    private String place;
    /**
     * 性别
     * 1：男
     * 2：女
     */
    @Column(length = 1)
    private Integer sex;
    /**
     *年龄
     */
    @Column(length = 3)
    private Integer age;
    /**
     * 电话号码
     */
    private String phone;
    /**
     *用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     *密码
     */
    private String password;
    /**
     *绑定状态
     * 1：已经关注
     * 2：取消关注
     * 3：已经绑定
     * 4：取消绑定
     */
    private Integer bindingStatus;
    /**
     * 关注时间
     */
    private Date attentionDate;
    /**
     * 绑定时间
     */
    private Date bindingDate;
    /**
     * 创建时间
     */
    private Date createDate;

    /** 是否为管理员,0:否，1：是 */
    @Column(name = "is_admin")
    private Integer isAdmin;

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBindingStatus() {
        return bindingStatus;
    }

    public void setBindingStatus(Integer bindingStatus) {
        this.bindingStatus = bindingStatus;
    }

    public Date getAttentionDate() {
        return attentionDate;
    }

    public void setAttentionDate(Date attentionDate) {
        this.attentionDate = attentionDate;
    }

    public Date getBindingDate() {
        return bindingDate;
    }

    public void setBindingDate(Date bindingDate) {
        this.bindingDate = bindingDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
