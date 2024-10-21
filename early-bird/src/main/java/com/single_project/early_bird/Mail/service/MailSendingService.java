package com.single_project.early_bird.Mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSendingService {
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject("Early Bird 회원가입 이메일 인증번호 입니다!");
        message.setText("회원님의 인증번호는 " + verificationCode + " 입니다.\n인증번호 유효시간은 10분 입니다.\n인증번호를 입력해주세요!");
        javaMailSender.send(message);
    }
}

