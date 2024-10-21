package com.single_project.early_bird.Mail.service;

import com.single_project.early_bird.Mail.dto.SendRequestDto;
import com.single_project.early_bird.Mail.dto.VerifyRequestDto;
import com.single_project.early_bird.Redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final RedisUtil redisUtil;
    private final MailSendingService mailSendingService;

    // 이메일 인증 코드 생성 및 저장
    public void createVerificationCode(SendRequestDto request) {
        // 인증 번호 생성
        String code = redisUtil.createdCertifyNum();

        // Redis에 인증 번호 저장 (키, 벨류)
        redisUtil.createRedisData(request.getTo(), code);

        // 인증 번호를 이메일로 발송
        mailSendingService.sendSimpleMessage(request.getTo(), code);
    }

    // 이메일 인증 코드 검증
    public boolean verifyCode(VerifyRequestDto request) {
        // Redis 에서 저장된 인증 코드 조회
        String verificationCode = redisUtil.getData(request.getTo());

        // 인증 코드 검증
        return verificationCode != null && verificationCode.equals(request.getVerificationCode());


    }
}

