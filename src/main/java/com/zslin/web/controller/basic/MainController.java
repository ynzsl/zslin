package com.zslin.web.controller.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zsl-pc on 2016/9/6.
 */
@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
