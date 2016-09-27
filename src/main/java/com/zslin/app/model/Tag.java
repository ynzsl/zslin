package com.zslin.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zsl-pc on 2016/9/27.
 */
@Entity
@Table(name = "t_tag")
public class Tag {

    @Id
    private Integer id;

    /** 名称 */
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
