package com.zslin.app.service;

import com.zslin.app.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2016/10/17 9:10.
 */
public interface ICommentService extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    @Query("FROM Comment c WHERE c.isShow=1 AND c.artId=?1")
    Page<Comment> findAll(Integer artId, Pageable pageable);
}
