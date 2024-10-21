package com.single_project.early_bird.User.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Global.exception.InvalidCredentialsException;
import com.single_project.early_bird.User.dto.SignInRequest;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean checkLoginIdDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean checkVerifiedByEmail(String email) {
        return userRepository.findVerifiedByEmail(email);
    }

    public void signInUser (SignInRequest request) {
        if(checkLoginIdDuplicate(request.getEmail())) throw new BadRequestException("이미 존재하는 이메일입니다.");
        if(checkVerifiedByEmail(request.getEmail())) throw new InvalidCredentialsException("이메일 인증을 완료해주세요.");


        // TODO : 이메일 암호화 작업 추가
        // SignInRequest 에 정의된 SignInRequestToEntity 활용해 객체 전환
        User user = request.SignInRequestToEntity();

        userRepository.save(user);
    }
}
