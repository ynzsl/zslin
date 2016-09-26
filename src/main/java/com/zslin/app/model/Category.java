package com.zslin.app.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zsl-pc on 2016/9/27.
 */
@Entity
@Table(name = "t_category")
public class Category {

    private Integer id;

    /** 名称 */
    private String name;

    /** 描述 */
    private String describe;

    /** 图标，采用bootstrap或fontawesome.io */
    private String icon;
}
