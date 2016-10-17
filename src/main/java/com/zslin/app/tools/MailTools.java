package com.zslin.app.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by 钟述林 393156105@qq.com on 2016/10/17 16:10.
 */
@Component
public class MailTools {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /** 发送邮件 */
    public void send(String title, String content, String ... to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(title);
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}
