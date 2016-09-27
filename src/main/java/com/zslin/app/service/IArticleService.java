package com.zslin.app.service;

import com.zslin.app.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by zsl-pc on 2016/9/27.
 */
public interface IArticleService extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {
}
