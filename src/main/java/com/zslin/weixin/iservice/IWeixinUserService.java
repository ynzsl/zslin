package com.zslin.weixin.iservice;

import com.zslin.weixin.model.WeixinUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin on 2016/9/18.
 */
public interface IWeixinUserService extends JpaRepository<WeixinUser, Integer>,JpaSpecificationExecutor<WeixinUser>{

    public WeixinUser findByOpenId(String openId);

    @Modifying
    @Transactional
    @Query("UPDATE WeixinUser wx SET wx.bindingStatus=?2 WHERE wx.openId=?1")
    public void updateStatus(String openId, Integer status);

    @Modifying
    @Transactional
    @Query("UPDATE WeixinUser wx SET wx.isAdmin=?2 WHERE wx.id=?1")
    public void updateIsAdmin(Integer id, Integer isAdmin);

    @Query("SELECT wx.openId FROM WeixinUser wx WHERE wx.isAdmin=1")
    public List<String> findAdmin();

    @Query("SELECT COUNT(id) FROM WeixinUser")
    public Long queryCount();
}
