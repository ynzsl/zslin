package com.zslin.weixin.iservice;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by zsl-pc on 2016/9/26.
 */
@Component
@Transactional
public class WeixinMenuServiceImpl {

    @PersistenceContext
    EntityManager em;

    public void updateSort(Integer [] ids) {
        int index = 1;
        for(Integer id : ids) {
            em.createQuery("update WeixinMenu m set m.orderNo="+(index++)+" WHERE m.id="+id).executeUpdate();
        }
    }
}
