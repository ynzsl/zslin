package com.zslin.weixin.iservice;

import com.zslin.weixin.model.WeixinMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by admin on 2016/9/20.
 */
public interface IWeixinMenuService extends JpaRepository<WeixinMenu,Integer>, JpaSpecificationExecutor<WeixinMenu> {

    @Query("from WeixinMenu wm where wm.pid is null")
    public List<WeixinMenu> findAllPidIsNull();

    @Query("from WeixinMenu wm where wm.pid=?1")
    public List<WeixinMenu> findSonMenuByPid(Integer pid);

    @Query("SELECT MAX(m.orderNo) FROM WeixinMenu m WHERE m.pid IS NULL")
    public Integer queryMaxOrderNo();

    @Query("SELECT MAX(m.orderNo) FROM WeixinMenu m WHERE m.pid=?1")
    public Integer queryMaxOrderNo(Integer pid);
}
