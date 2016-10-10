package com.zslin.app.service;

import com.zslin.app.dto.CateDto;
import com.zslin.app.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsl-pc on 2016/9/27.
 */
public interface IArticleService extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    @Query("SELECT new com.zslin.app.dto.CateDto(a.cateId AS cateId, a.cateName AS cateName, COUNT(id) as amount) FROM Article a WHERE a.isShow=1 GROUP BY a.cateId")
    public List<CateDto> queryCates();

    @Query("UPDATE Article a SET a.readCount=a.readCount+1 WHERE a.id=?1")
    @Modifying
    @Transactional
    public void updateReadCount(Integer id);
}
