package com.zslin.weixin.vo;

import java.util.List;

/**
 * Created by admin on 2016/9/25.
 */
public class Button {

    private String type;
    private String name;
    private String url;
    private List<Sub_button> sub_button;

    public List<Sub_button> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<Sub_button> sub_button) {
        this.sub_button = sub_button;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
