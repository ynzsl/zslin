package com.zslin.test;

import com.zslin.app.dto.CateDto;
import com.zslin.app.service.IArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zsl-pc on 2016/10/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("zsl")
public class CateTest {

    @Autowired
    private IArticleService articleService;

    @Test
    public void test() {
        List<CateDto> list = articleService.queryCates();
        System.out.println(list.size());
    }
}
