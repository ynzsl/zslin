package com.zslin.weixin.iservice;

import com.zslin.weixin.model.WeixinMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zsl-pc on 2016/9/26.
 */
public interface IWeixinMessageService extends JpaRepository<WeixinMessage, Integer>, JpaSpecificationExecutor<WeixinMessage> {

    @Query("SELECT COUNT(id) FROM WeixinMessage")
    public Long queryCount();
}
