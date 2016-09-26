package com.zslin.weixin.iservice;

import com.zslin.weixin.model.WeixinConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2016/9/19.
 */
@Service("weiXinConfigService")
public interface IWeixinConfigService extends JpaRepository<WeixinConfig,Integer>{
}
