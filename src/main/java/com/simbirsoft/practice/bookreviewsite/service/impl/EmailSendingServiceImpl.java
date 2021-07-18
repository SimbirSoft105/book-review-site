package com.simbirsoft.practice.bookreviewsite.service.impl;

import com.simbirsoft.practice.bookreviewsite.service.EmailSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class EmailSendingServiceImpl implements EmailSendingService {

    @Value("spring.mail.username")
    private String from;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private ExecutorService executorService;

    @Override
    public void sendEmail(String to, String letter, String subject) {

        executorService.execute(() -> javaMailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(letter, true);
        }));

    }
}
