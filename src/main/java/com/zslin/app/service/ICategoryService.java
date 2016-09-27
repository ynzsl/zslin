package com.zslin.app.service;

import com.zslin.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zsl-pc on 2016/9/27.
 */
public interface ICategoryService extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    @Modifying
    @Transactional
    @Query("update Category c set c.orderNo=?2 WHERE c.id=?1")
    public void updateOrderNo(Integer id, Integer orderNo);
}
