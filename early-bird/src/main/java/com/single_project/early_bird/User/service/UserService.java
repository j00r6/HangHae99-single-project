package com.single_project.early_bird.User.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Global.exception.InvalidCredentialsException;
import com.single_project.early_bird.Global.exception.UserNotFoundException;
import com.single_project.early_bird.Mail.service.MailVerificationService;
import com.single_project.early_bird.User.dto.SignInRequest;
import com.single_project.early_bird.User.entity.Authority;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public boolean checkLoginIdDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean checkVerifiedByEmail(String email) {
        return userRepository.findVerifiedByEmail(email);
    }

    public void signInUser (SignInRequest request) {
        if(checkLoginIdDuplicate(request.getEmail())) throw new BadRequestException("이미 존재하는 이메일입니다.");
        if(checkVerifiedByEmail(request.getEmail())) throw new InvalidCredentialsException("이메일 인증을 완료해주세요.");


        // TODO : 비밀번호 외에 정보 암호화 구현
        // SignInRequest 에 정의된 SignInRequestToEntity 활용해 객체 전환
        User user = request.SignInRequestToEntity(encoder.encode(request.getPassword()));

        // 회원가입 후 인증
        user.setMailVerified(true);

        // 클라이언트에서 유저가 선택한 회원 권한에 따라 다른 ROLE 입력
        Authority userRole = Authority.builder().name("ROLE_USER").build();
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);
    }

    public User findUserByPrincipal(String principal) {
        Optional<User> optionalUser = userRepository.findByEmail(principal);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findVerifyUser (Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User findUserById =
                optionalUser.orElseThrow(() ->
                        new UserNotFoundException("회원 정보가 존재하지 않습니다"));
        return findUserById;
    }

    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User findUserByEmail =
                optionalUser.orElseThrow(() ->
                        new UserNotFoundException("회원 정보가 존재하지 않습니다"));
        return findUserByEmail;
    }
}
