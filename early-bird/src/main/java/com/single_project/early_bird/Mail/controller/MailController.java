package com.single_project.early_bird.Mail.controller;

import com.single_project.early_bird.Mail.dto.SendRequestDto;
import com.single_project.early_bird.Mail.dto.VerifyRequestDto;
import com.single_project.early_bird.Mail.service.MailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    private final MailVerificationService mailVerificationService;

    // 이메일 인증 코드 생성 및 저장
    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody SendRequestDto request) {
        mailVerificationService.createVerificationCode(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("입력하신 " + request.getTo() + " 메일로 인증번호가 발송되었습니다. 인증 코드는 10분간 유효합니다.");
    }

    // 이메일 인증 코드 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyRequestDto request) {
        boolean isVerified = mailVerificationService.verifyCode(request);
        if (isVerified) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(request.getTo() + " 해당 메일이 인증에 성공하였습니다!");
        } else {
            //TODO : 상황별 예외처리 추가 구현 필요
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(request.getTo() + " 해당 메일이 인증에 실패하였습니다! 자세한 내용은 관리자에게 문의해주세요");
        }
    }
}
