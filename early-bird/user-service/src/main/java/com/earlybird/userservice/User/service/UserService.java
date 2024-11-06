package com.earlybird.userservice.User.service;

import com.earlybird.userservice.Global.exception.BadRequestException;
import com.earlybird.userservice.Global.exception.UserNotFoundException;
import com.earlybird.userservice.User.dto.SignInRequest;
import com.earlybird.userservice.User.entity.Authority;
import com.earlybird.userservice.User.entity.User;
import com.earlybird.userservice.User.repository.UserRepository;
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

    public void signInUser (SignInRequest request) {
        if(checkLoginIdDuplicate(request.getEmail())) throw new BadRequestException("이미 존재하는 이메일입니다.");

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
