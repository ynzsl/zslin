package com.zslin.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2016/10/17 9:37.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("zsl")
public class TestMail {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Test
    public void testSend() {
        System.out.println("=================="+from);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@zslin.com");
        message.setTo("398986099@qq.com");
        message.setSubject("测试1");
        message.setText("测试，估计是内容");

        mailSender.send(message);
    }
}
